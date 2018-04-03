package com.gjzg.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gjzg.R;

public class PullToRefreshLayout extends RelativeLayout {

    /**
     * 头
     */
    private View headView;//头视图
    private ImageView headIv;//头图标
    private TextView headTv;//头文字
    private int headHeight;//头高度
    private float headBorder;//头临界
    /**
     * 拉
     */
    private View pullView;//拉视图
    private int pullHeight;//拉高度
    private int pullWidth;//拉宽度

    /**
     * 尾
     */
    private View footView;//尾视图
    private ImageView footIv;//尾图标
    private TextView footTv;//尾文字
    private int footHeight;//尾高度
    private float footBorder;//尾临界

    /**
     * 状态
     */
    public static final int INIT = 0;//初始
    public static final int RELEASE_TO_REFRESH = 1;//释放刷新
    public static final int REFRESHING = 2;//正在刷新
    public static final int RELEASE_TO_LOAD = 3;//释放加载
    public static final int LOADING = 4;//正在加载
    public static final int DONE = 5;//完成
    private int state = INIT;

    /**
     * 接口
     */
    private OnRefreshListener mListener;

    private float downY;//按下时Y坐标
    private float lastY;//上一个Y坐标
    private float pullDownY = 0;//下拉偏移量
    private float pullUpY = 0;//上拉偏移量
    private int offset;//偏移量

    /**
     * 动画
     */
    private RotateAnimation rotateAnimation;

    /**
     * 线程
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case 1:
                        headIv.clearAnimation();
                        break;
                    case 2:
                        footIv.clearAnimation();
                        break;
                    default:
                        break;
                }
                pullDownY = 0;
                pullUpY = 0;
                requestLayout();
                state = INIT;
                refreshViewByState();
                isTouch = true;
            }
        }
    };

    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
    }

    private void refreshViewByState() {
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                headIv.setImageResource(R.mipmap.pull_w);
                headTv.setText(R.string.pull_to_refresh);
                // 上拉布局初始状态
                footIv.setImageResource(R.mipmap.release_w);
                footTv.setText(R.string.pullup_to_load);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                headIv.setImageResource(R.mipmap.release_w);
                headTv.setText(R.string.release_to_refresh);
                break;
            case REFRESHING:
                // 正在刷新状态
                headIv.setImageResource(R.mipmap.refresh_w);
                headTv.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                footIv.setImageResource(R.mipmap.pull_w);
                footTv.setText(R.string.release_to_load);
                break;
            case LOADING:
                // 正在加载状态
                footIv.setImageResource(R.mipmap.refresh_w);
                footTv.setText(R.string.loading);
                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                lastY = downY;
                mEvents = 0;
                releasePull();
                if (state != REFRESHING && state != LOADING) {
                    isTouch = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvents == 0) {
                    if (pullDownY > 0 || (((Pullable) pullView).canPullDown()
                            && canPullDown && state != LOADING && state != REFRESHING)) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight()) {
                            pullDownY = getMeasuredHeight();
                        }
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = false;
                        }
                    } else if (pullUpY < 0
                            || (((Pullable) pullView).canPullUp() && canPullUp && state != REFRESHING && state != LOADING)) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;

                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight()) {
                            pullUpY = -getMeasuredHeight();
                        }
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = false;
                        }
                    }
                }
                if (isTouch) {
                    lastY = ev.getY();
                    if (pullDownY > 0 || pullUpY < 0) {
                        requestLayout();
                    }
                    if (pullDownY > 0) {
                        if (pullDownY <= headBorder && (state == RELEASE_TO_REFRESH || state == DONE)) {
                            // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                            state = INIT;
                            refreshViewByState();
                        }
                        if (pullDownY >= headBorder && state == INIT) {
                            // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                            state = RELEASE_TO_REFRESH;
                            refreshViewByState();
                        }
                    } else if (pullUpY < 0) {
                        // 下面是判断上拉加载的，同上，注意pullUpY是负值
                        if (-pullUpY <= footBorder && (state == RELEASE_TO_LOAD || state == DONE)) {
                            state = INIT;
                            refreshViewByState();
                        }
                        // 上拉操作
                        if (-pullUpY >= footBorder && state == INIT) {
                            state = RELEASE_TO_LOAD;
                            refreshViewByState();
                        }
                    }
                    // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                    // Math.abs(pullUpY))就可以不对当前状态作区分了
                    if ((pullDownY + Math.abs(pullUpY)) > 8) {
                        // 防止下拉过程中误触发长按事件和点击事件
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > headBorder || -pullUpY > footBorder) {
                    // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                    isTouch = false;
                }
                if (state == RELEASE_TO_REFRESH) {
                    state = REFRESHING;
                    refreshViewByState();
                    // 刷新操作
                    if (mListener != null) {
                        canPullDown = false;
                        pullDownY = headBorder;
                        pullUpY = 0;
                        requestLayout();
                        headIv.startAnimation(rotateAnimation);
                        mListener.onRefresh(this);
                    }
                } else if (state == RELEASE_TO_LOAD) {
                    state = LOADING;
                    refreshViewByState();
                    // 加载操作
                    if (mListener != null) {
                        canPullUp = false;
                        pullDownY = 0;
                        pullUpY = -footBorder;
                        requestLayout();
                        footIv.startAnimation(rotateAnimation);
                        mListener.onLoadMore(this);
                    }
                } else {
                    pullDownY = 0;
                    pullUpY = 0;
                    requestLayout();
                }
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    public void hideHeadView() {
        handler.sendEmptyMessage(1);
    }

    public void hideFootView() {
        handler.sendEmptyMessage(2);
    }

    private void initView() {
        // 初始化下拉布局
        headIv = (ImageView) headView.findViewById(R.id.iv_head);
        headTv = (TextView) headView.findViewById(R.id.tv_head);
        //初始化上拉布局
        footIv = (ImageView) footView.findViewById(R.id.iv_foot);
        footTv = (TextView) footView.findViewById(R.id.tv_foot);
        refreshViewByState();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            headView = getChildAt(0);
            pullView = getChildAt(1);
            footView = getChildAt(2);
            headBorder = ((ViewGroup) headView).getChildAt(0).getMeasuredHeight();
            footBorder = ((ViewGroup) footView).getChildAt(0).getMeasuredHeight();
            headHeight = headView.getMeasuredHeight();
            pullHeight = pullView.getMeasuredHeight();
            footHeight = footView.getMeasuredHeight();
            pullWidth = pullView.getMeasuredWidth();
            initView();
            isLayout = true;
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        offset = (int) (pullDownY + pullUpY);
        headView.layout(0, -headHeight + offset, pullWidth, offset);
        pullView.layout(0, offset, pullWidth, pullHeight + offset);
        footView.layout(0, pullHeight + offset, pullWidth, pullHeight + footHeight + offset);
    }

    public interface OnRefreshListener {
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

}
