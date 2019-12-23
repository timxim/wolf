package study.daydayup.wolf.business.trade.api.state.loan;


import study.daydayup.wolf.business.trade.api.state.AbstractTradeState;
import study.daydayup.wolf.business.trade.api.state.TradeState;

/**
 * study.daydayup.wolf.business.trade.api.state
 *
 * @author Wingle
 * @since 2019/10/5 11:23 PM
 **/

public class PaidByProxyState extends AbstractTradeState implements TradeState {
    protected int code = 11;
    private String name = "已支付";
}
