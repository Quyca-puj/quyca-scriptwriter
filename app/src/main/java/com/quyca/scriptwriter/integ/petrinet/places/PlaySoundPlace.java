package com.quyca.scriptwriter.integ.petrinet.places;

import android.media.MediaPlayer;

import com.quyca.scriptwriter.integ.utils.UIBundle;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.model.SoundAction;

import java.io.FileDescriptor;
import java.io.IOException;

public class PlaySoundPlace extends PlayViewPlace {


    public PlaySoundPlace(Playable playable, UIBundle bundle) {
        super(playable, bundle);
    }

    @Override
    public void run() {
        done = true;
        SoundAction sAction = (SoundAction) playable;
        playable.setDone(QuycaCommandState.IN_EXECUTION);
        refreshUI();
        MediaPlayer player = new MediaPlayer();
        try {
            FileDescriptor file = sAction.getSoundFile(context);
            player.setDataSource(file);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
            done = false;
        } finally {
            sAction.setSound(null);
            player.reset();
            player.release();
        }
        if(done){
            fireTransitions();
        }
        playable.setDone(done ? QuycaCommandState.DONE : QuycaCommandState.ERROR);
        refreshUI();
    }
}
