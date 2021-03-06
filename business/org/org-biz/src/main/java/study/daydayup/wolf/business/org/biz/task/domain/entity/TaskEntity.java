package study.daydayup.wolf.business.org.biz.task.domain.entity;

import lombok.NonNull;
import study.daydayup.wolf.business.org.api.task.domain.entity.Task;
import study.daydayup.wolf.business.org.api.task.domain.entity.task.*;
import study.daydayup.wolf.business.org.api.task.domain.enums.task.CollectionStateEnum;
import study.daydayup.wolf.business.org.api.task.domain.event.TaskEvent;
import study.daydayup.wolf.framework.layer.domain.AbstractEntity;
import study.daydayup.wolf.framework.layer.domain.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * study.daydayup.wolf.business.org.biz.task.domain.entity
 *
 * @author Wingle
 * @since 2020/3/15 11:42 下午
 **/
public class TaskEntity extends AbstractEntity<Task> implements Entity {
    private LocalDateTime now;

    public TaskEntity(Task task) {
        this(task, true);
    }

    public TaskEntity(Task task, boolean isNew) {
        this.isNew = isNew;

        model = task;
        key   = Task.builder()
                .id(task.getId())
                .orgId(task.getOrgId())
                .staffId(task.getStaffId())
                .taskType(task.getTaskType())
                .build();
    }

    public void format() {
        formatTask();
        formatContact();
        formatTrade();
        formatScheduler();
    }

    public void assign(Long staffId) {
        initChanges();
        model.setStaffId(staffId);
        changes.setStaffId(staffId);

        addAssignmentLog();
    }

    //TODO add task state machine
    public void changeState(TaskEvent event) {

    }

    public void changeState(Integer state) {
        initChanges();
        model.setState(state);
        changes.setState(state);

        addStateLog();
    }

    public void changePayingAmount(@NonNull BigDecimal amount) {
        initTradeKey();
        initTradeChanges();
        changes.getTrade().setPayingAmount(amount);
    }

    public void changePaidAmount(@NonNull BigDecimal amount) {
        if (null == model.getTrade()) {
            throw new IllegalArgumentException("Can't change paidAmount when model.trade isNull");
        }

        initTradeKey();
        initTradeChanges();

        TaskTrade trade = model.getTrade();
        BigDecimal paidAmount = trade.getPaidAmount();
        paidAmount = paidAmount.add(amount);

        changes.getTrade().setPaidAmount(paidAmount);
        changes.getTrade().setPayingAmount(BigDecimal.ZERO);
    }

    private void initTradeKey() {
        if (null != key.getTrade()) {
            return;
        }

        TaskTrade trade = TaskTrade.builder()
                .orgId(model.getOrgId())
                .taskId(model.getId())
                .build();
        key.setTrade(trade);
    }

    private void initTradeChanges() {
        initChanges();
        if (null != changes.getTrade()) {
            return;
        }

        TaskTrade trade = new TaskTrade();
        changes.setTrade(trade);
    }

    private void initNow() {
        if (now != null) {
            return;
        }

        now = LocalDateTime.now();
    }

    private void initChanges() {
        if (changes != null) {
            return;
        }

        changes = new Task();
    }

    private void formatTask() {

    }

    private void formatContact() {
        TaskContact contact = model.getContact();
        if (null == contact) {
            return;
        }

        contact.setOrgId(model.getOrgId());
        contact.setStaffId(model.getStaffId());
        contact.setTaskId(model.getId());
    }

    private void formatTrade() {
        TaskTrade trade = model.getTrade();
        if (null == trade) {
            return;
        }

        trade.setOrgId(model.getOrgId());
        trade.setStaffId(model.getStaffId());
        trade.setTaskId(model.getId());
    }

    private void formatScheduler() {
        TaskScheduler scheduler = model.getScheduler();
        if (scheduler == null) {
            return;
        }

        scheduler.setOrgId(model.getOrgId());
        scheduler.setStaffId(model.getStaffId());
        scheduler.setTaskId(model.getId());
    }

    private void addStateLog() {
        if (null == changes.getState()) {
            return;
        }

        initNow();
        TaskStateLog log = TaskStateLog.builder()
                .orgId(model.getOrgId())
                .staffId(model.getStaffId())
                .taskId(model.getId())
                .projectId(model.getProjectId())

                .sourceState(key.getState())
                .targetState(changes.getState())
                .sourceVersion(model.getVersion())
                .targetVersion(model.getVersion() + 1)

                .createdAt(now)
                .build();

        changes.setStateLog(log);
    }

    private void addAssignmentLog() {
        if (null == changes.getStaffId()) {
            return;
        }

        initNow();
        TaskAssignmentLog log = TaskAssignmentLog.builder()
                .orgId(model.getOrgId())
                .taskId(model.getId())
                .projectId(model.getProjectId())

                .sourceOwner(key.getStaffId())
                .targetOwner(changes.getStaffId())
                .sourceVersion(model.getVersion())
                .targetVersion(model.getVersion() + 1)

                .createdAt(now)
                .build();

        changes.setAssignmentLog(log);
    }

}

