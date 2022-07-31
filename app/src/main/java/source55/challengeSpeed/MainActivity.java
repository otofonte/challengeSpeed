package source55.challengeSpeed;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    ChallengeSpeed game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new ChallengeSpeed(this);
        setContentView(game);
    }

    @Override
    public void onBackPressed() {
        if (game.time == -1) super.onBackPressed();
        else game.time = -1;
    }
}