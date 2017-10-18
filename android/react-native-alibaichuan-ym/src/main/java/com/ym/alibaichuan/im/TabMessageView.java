package com.ym.alibaichuan.im;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;

/**
 * 最近会话界面的定制点(根据需要实现相应的接口来达到自定义会话列表界面)，不设置则使用openIM默认的实现
 * 调用方设置的回调，必须继承BaseAdvice 根据不同的需求实现 不同的 开放的 Advice
 * com.alibaba.mobileim.aop.pointcuts包下开放了不同的Advice.通过实现多个接口，组合成对不同的ui界面的定制
 * 这里设置了自定义会话的定制
 * 1.CustomConversationAdvice 实现自定义会话的ui定制
 * 2.CustomConversationTitleBarAdvice 实现自定义会话列表的标题的ui定制
 * <p/>
 * 另外需要在application中将这个Advice绑定。设置以下代码
 * AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_POINTCUT, CustomChattingAdviceDemo.class);
 *
 * @author jing.huai
 */
public class TabMessageView extends IMConversationListUI {

    private static final String TAG = "ConversationListUICustomSample";

    public TabMessageView(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 返回会话列表的自定义标题
     *
     * @param fragment
     * @param context
     * @param inflater
     * @return
     */
//    @Override
//    public View getCustomConversationListTitle(final Fragment fragment,
//                                               final Context context, LayoutInflater inflater) {
//        //TODO 重要：必须以该形式初始化customView---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局xml**中定义的高度和宽度无效，均被默认的wrap_content替代
//        RelativeLayout customView = (RelativeLayout) inflater
//                .inflate(R.layout.common_title, new RelativeLayout(context),false);
//        TextView title = (TextView) customView.findViewById(R.id.common_title_text);
//        final YWIMKit mIMKit = LoginSampleHelper.getInstance().getIMKit();
//
//        title.setText("消息");
//        title.setTextColor(Color.WHITE);
//        final String loginUserId = LoginSampleHelper.getInstance().getIMKit().getIMCore().getLoginUserId();
//        final String appKey = LoginSampleHelper.getInstance().getIMKit().getIMCore().getAppKey();
//        if(TextUtils.isEmpty(loginUserId)|| TextUtils.isEmpty(appKey)){
//            title.setText("未登录");
//        }
////        showRightTv(customView, "全部已读", new OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mIMKit.getConversationService().markAllReaded();
////            }
////        });
//        showBackBtn(customView,fragment);
//        return customView;
//    }
//    public void showRightTv(View v, String str, OnClickListener onClickListener) {
//        FrameLayout rightLayout = (FrameLayout) v.findViewById(R.id.common_title_tv_right);
//        TextView iv = (TextView) v.findViewById(R.id.right_btn);
//        iv.setText(str);
//        rightLayout.setOnClickListener(onClickListener);
//        rightLayout.setVisibility(View.VISIBLE);
//    }
//
//    public void showBackBtn(View v, final Fragment fragment) {
//        FrameLayout backLayout = (FrameLayout) v.findViewById(R.id.common_title_btn_back);
//        backLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragment.getActivity().finish();
//            }
//        });
//        backLayout.setVisibility(View.VISIBLE);
//    }
    /**
     * 是否支持下拉刷新
     */
    @Override
    public  boolean getPullToRefreshEnabled(){
        return true;
    }
    /**
     * 返回自定义置顶回话的背景色(16进制字符串形式)
     * @return
     */
    @Override
    public String getCustomTopConversationColor() {
        return "#e1f5fe";
    }

    @Override
    public boolean enableSearchConversations(Fragment fragment){
        return true;
    }

    /**
     * 该方法可以构造一个会话列表为空时的展示View
     * @return
     *      empty view
     */
    @Override
    public View getCustomEmptyViewInConversationUI(Context context) {
        /** 以下为示例代码，开发者可以按需返回任何view*/
        TextView textView = new TextView(context);
        textView.setText("还没有会话哦，快去找人聊聊吧!");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        return textView;
    }

}
