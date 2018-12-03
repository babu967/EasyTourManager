package com.codex.easytourmanager.event_class;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class Moments implements Serializable {
    private String momentImage;
    private String momentCaption;
    private String momentKey;
    private String momentdateTime;
    private String eventKey;

    public Moments() {
    }

    public Moments(String momentImage, String momentCaption, String momentKey, String momentdateTime, String eventKey) {
        this.momentImage = momentImage;
        this.momentCaption = momentCaption;
        this.momentKey = momentKey;
        this.momentdateTime = momentdateTime;
        this.eventKey = eventKey;
    }

    public String getMomentImage() {
        return momentImage;
    }

    public String getMomentCaption() {
        return momentCaption;
    }

    public String getMomentKey() {
        return momentKey;
    }

    public String getMomentdateTime() {
        return momentdateTime;
    }

    public String getEventKey() {
        return eventKey;
    }


    public Bitmap retrieveMomentImage() {
        byte[] bytes = Base64.decode(momentImage,0);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
