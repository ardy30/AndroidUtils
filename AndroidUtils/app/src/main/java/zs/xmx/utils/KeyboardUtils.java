package zs.xmx.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @创建时间   2016/9/25 21:16
 * @本类描述	  软键盘_相关工具类
 * @内容说明   1.动态隐藏软键盘
 *            2.动态显示软键盘
 *            3.切换键盘显示与否状态(EditText 切换软键盘显示)
 *            4.点击屏幕空白区域隐藏软键盘 (需要拷这个类里面的方法进去)
 *
 * 注意:
 *    1.这个类的方法需要在View完全显示后才能调用
 *         建议用在"按钮",或使用线程使用
 *    2.InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen=imm.isActive(); 中
		     isOpen 一直为true ,没用
 *
 * ---------------------------------------------     
 * @更新时间   2016/9/25 
 * @更新说明
 */
public class KeyboardUtils {
    /**
     * <--->
     *     android:windowSoftInputMode属性:
     *
     *     这个属性能影响两个事情:
     *     【一】当有焦点产生时，软键盘是隐藏还是显示
     *     【二】是否减少活动主窗口大小以便腾出空间放软键盘
     *
     *     他的设置必须是下面的一个值,或者"state...| adjust.."
     *     --------------------------------------------------
     *     【1】stateUnspecified：软键盘的状态并没有指定，系统将选择一个合适的状态或依赖于主题的设置
     *     【2】stateUnchanged：当这个activity出现时，软键盘将一直保持在上一个activity里的状态，无论是隐藏还是显示
     *     【3】stateHidden：用户选择activity时，软键盘总是被隐藏
     *     【4】stateAlwaysHidden：当该Activity主窗口获取焦点时，软键盘也总是被隐藏的
     *     【5】stateVisible：软键盘通常是可见的
     *     【6】stateAlwaysVisible：用户选择activity时，软键盘总是显示的状态
     *     【7】adjustUnspecified：默认设置，通常由系统自行决定是隐藏还是显示
     *     【8】adjustResize：该Activity总是调整屏幕的大小以便留出软键盘的空间
     *     【9】adjustPan：当前窗口的内容将自动移动以便当前焦点从不被键盘覆盖和用户能总是看到输入内容的部分

     *
     * </--->
     */

    /**
     * 避免输入法面板遮挡:(Android 1.5后的一个新特性)
     * 在manifest.xml中activity中设置:
     *       android:windowSoftInputMode="stateVisible|adjustResize"
     *
     * 让界面不被软键盘挤上去
     * 在manifest.xml中activity中设置:
     *       android:windowSoftInputMode="adjustPan"
     */

    /**
     * 动态隐藏软键盘
     * <p>
     * inputmanger.hideSoftInputFromWindow(windowToken, flags);
     * <p>
     * 参数:
     * windowToken 由窗口请求View.getWindowToken() 返回得到的令牌(token)
     * flags 提供额外的操作标志.(0 ; HIDE_IMPLICIT_ONLY )
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        //得到最上层的window
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * EditView 动态隐藏软键盘
     *
     * @param context 上下文
     * @param edit    输入框
     */
    public static void hideSoftInput(Context context, EditText edit) {
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法1）
     * <p>
     * 在onTouch中处理，未获焦点则隐藏
     * <p>
     * 参照以下注释代码
     */
    public static void clickBlankArea2HideSoftInput0() {
        Log.i("tips", "U should copy the following code.");
        /*
        @Override
        public boolean onTouchEvent (MotionEvent event){
            if (null != this.getCurrentFocus()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
            return super.onTouchEvent(event);
        }
        */
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法2）
     * <p>
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     * <p>
     * 需重写dispatchTouchEvent
     * <p>
     * 参照以下注释代码
     */
    public static void clickBlankArea2HideSoftInput1() {
        Log.i("tips", "U should copy the following code.");
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    hideKeyboard(v.getWindowToken());
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }

        // 获取InputMethodManager，隐藏软键盘
        private void hideKeyboard(IBinder token) {
            if (token != null) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        */
    }

    /**
     * EditText 动态显示软键盘
     * <p>
     * inputManager.showSoftInput(view, flags);
     * <p>
     * 参数:
     * view 当前焦点视图
     * flags 提供额外的操作标志(0 ; SHOW_IMPICIT)
     *
     * @param context 上下文
     * @param edit    输入框
     */
    public static void showSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }

    /**
     * 切换键盘显示与否状态( EditText 切换软键盘显示)
     *
     * @param context 上下文
     * @param edit    输入框
     */
    public static void toggleSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
