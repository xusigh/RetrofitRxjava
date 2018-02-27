package baseframes.base;

import android.animation.ObjectAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.widget.ImageView;

/**
 * Created by zhanghs on 2017/12/27/027.
 */

public class TransitionTest extends Transition {
    private ImageView iv_1,iv_2,iv_3,iv_4;
    private ObjectAnimator objectAnimator;
    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        iv_1=transitionValues.view.findViewById(R.id.iv_1);
        iv_2=transitionValues.view.findViewById(R.id.iv_2);
        iv_3=transitionValues.view.findViewById(R.id.iv_3);
        iv_4=transitionValues.view.findViewById(R.id.iv_4);
        objectAnimator=ObjectAnimator.ofFloat(iv_1,"transitionY",1000);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {

    }
}
