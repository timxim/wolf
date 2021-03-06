package study.daydayup.wolf.dts.transformation;

import lombok.Getter;
import lombok.NonNull;
import study.daydayup.wolf.common.io.db.Row;
import study.daydayup.wolf.common.io.db.Table;
import study.daydayup.wolf.common.util.collection.CollectionUtil;
import study.daydayup.wolf.dts.sink.Sink;

import java.util.ArrayList;
import java.util.List;

/**
 * study.daydayup.wolf.framework.dts.transeformer
 *
 * @author Wingle
 * @since 2020/2/16 6:54 下午
 **/
public class DbTransformation implements Transformation {
    private Sink sink;
    private Statistics statistics;

    @Getter
    private Operator currentOperator;
    private List<Operator> operatorList;

    public static DbTransformation newTask(@NonNull Sink sink) {
        return new DbTransformation(sink);
    }

    private DbTransformation(Sink sink) {
        this.sink = sink;

        statistics = new Statistics();
        statistics.setKeyColumns(this.sink.getKeyColumns());

        operatorList = new ArrayList<>(5);
    }

    public Operator addJob() {
        Operator operator = new Operator(statistics);

        operatorList.add(operator);
        currentOperator = operator;

        return currentOperator;
    }

    public Statistics transform(Table table) {
        return transform(table, false);
    }

    public Statistics transform(Table table, boolean sqlFormat) {
        if (!CollectionUtil.notEmpty(table)) {
            return statistics;
        }

        for (Row row : table) {
            transform(row);
        }

        if (sqlFormat){
            //return format();
        }

        return statistics;
    }

    public Statistics merge(@NonNull Row row) {
        if (null == row || operatorList.isEmpty()) {
            return statistics;
        }

        for (Operator operator : operatorList) {
            operator.operate(row);
        }

        return statistics;
    }

    public Statistics format() {
        if (operatorList.isEmpty()) {
            return statistics;
        }

        for (Operator operator : operatorList) {
            operator.format();
        }

        return statistics;
    }

    private void transform(Row row) {
        if (null == row || operatorList.isEmpty()) {
            return;
        }

        if (isTransformed(row)) {
            return;
        }

        for (Operator operator : operatorList) {
            operator.operate(row);
        }
    }

    private boolean isTransformed(@NonNull Row row) {
        Long id = (Long) row.get(Table.DEFAULT_ID_COLUMN);
        return sink.isDuplicated(id);
    }

}
