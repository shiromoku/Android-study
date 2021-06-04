package com.example.allinone.view

    import android.content.Context;
    import android.os.Handler;
    import android.util.AttributeSet;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.ProgressBar;
    import android.widget.TextView;
    import com.example.allinone.R

abstract class Pop2FlushListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ListView(context, attrs, defStyleAttr) {

    val DOWN = 0;//下拉刷新
    val RELEASE = 1;//释放刷新
    val REFREASH = 2;//刷新中

    lateinit var view:View
    lateinit var iv:ImageView
    lateinit var tv:TextView
    lateinit var pb:ProgressBar
    var downY = 0
    var moveY = 0
    var now = DOWN
    var heights = 0

    abstract var onCallBack:OnCallBack?

    interface OnCallBack{
        fun callback()
    }



    init {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fr_3_header,null)
        iv = view.findViewById(R.id.fr_3_header_iv)
        pb = view.findViewById(R.id.fr_3_header_pb)
        tv = view.findViewById(R.id.fr_3_header_tv)
        //设置measure可让父容器不对子容器做任何约束
        view.measure(0,0)
        //获取view原始高速，view.getHeight()获得的是显示在屏幕中的高度，而我们这个头部View要隐藏在顶部
        heights = view.measuredHeight
        //设置位置
        view.setPadding(0,-height,0,0)
        //将view添加至ListView的头部view中
        addHeaderView(view)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when(ev.action){
                MotionEvent.ACTION_DOWN -> {
                    downY = ev.y.toInt()
                }
                MotionEvent.ACTION_DOWN -> {
                    moveY = ev.y.toInt()
                    val number = moveY - downY
                    //判断是否向下移动，getFirstVisiblePosition()判断是否在顶部，并且状态不能为刷新中
                    if(number >0 && firstVisiblePosition == 0 && now != REFREASH){
                        //头部View是否完全显示
                        val padding = number - height
                        //完全显示和未完全显示是不同状态
                        if(padding<50){
                            //改变现在的状态
                            now = DOWN
                            tv.text = "下拉刷新"
//                            iv.setImageResource(R.mipmap.pull)
                        }else{
                            now = RELEASE
                            tv.text = "松手刷新"
//                            iv.setImageResource(R.mipmap.push)
                        }
                        view.setPadding(0,padding,0,0)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(now == RELEASE){
                        //将状态设置为刷新中
                        now = REFREASH;
                        iv.visibility = GONE
                        pb.visibility = VISIBLE
                        tv.text = "正在刷新"
                        //将头部为位置设置在顶部
                        view.setPadding(0,0,0,0)
                        //执行回调
                        onCallBack?.callback()
                    }
                }
            }
        }
        return super.onTouchEvent(ev)
    }


    //初始化，一般在回调中调用这个方法
    fun finish(){
        now = DOWN;
        tv.text = "刷新成功"
//        iv.setImageResource(R.mipmap.ok)
        iv.visibility = VISIBLE
        pb.visibility = GONE
        Handler().postDelayed({
//            iv.setImageResource(R.mipmap.push)
            tv.text = "下拉刷新"
            view.setPadding(0,-height,0,0)
        },1000)
    }


}