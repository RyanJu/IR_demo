package com.alcidae.smarthome.ir.data;

import com.alcidae.smarthome.ir.data.db.IRBean;

/**
 * Create By zhurongkun
 *
 * @author zhurongkun
 * @version 2018/3/27 18:50 1.0
 * @time 2018/3/27 18:50
 * @project ir_demo com.alcidae.smarthome.ir.data
 * @description 当匹配遥控器成功时发送该事件
 * @updateVersion 1.0
 * @updateTime 2018/3/27 18:50
 */

public class EventMatchSuccess {
    IRBean irBean;

    public EventMatchSuccess(IRBean irBean) {
        this.irBean = irBean;
    }

    public IRBean getIrBean() {
        return irBean;
    }

    public void setIrBean(IRBean irBean) {
        this.irBean = irBean;
    }
}
