package com.quyca.scriptwriter.config;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una accion configurada para un robot actor en especifico. Generalmente se carga a partir de un archivo JSON de configuracion.
 *
 * @version 1.0
 * @since 1.0
 */
public class QuycaConfiguration implements Serializable {
    /**
     *
     */
    private List<ConfiguredEmotion> emotions;
    /**
     *
     */
    private List<ConfiguredAction> actions;


    private List<ConfiguredAction> extraActions;

    /**
     * Instantiates a new Quyca configuration.
     */
    public QuycaConfiguration() {
        emotions = new ArrayList<>();
        actions = new ArrayList<>();
        extraActions = new ArrayList<>();
    }

    public static QuycaConfiguration mergeConfWithBaseConf(QuycaConfiguration config) {
        QuycaConfiguration baseConf = getBasicConfig();
        QuycaConfiguration mergedConf = new QuycaConfiguration();
        if (config.getExtraActions() != null) {
            config.getExtraActions().forEach(configuredAction -> {
                Log.i("AnsTest",configuredAction.toString());
            });
            mergedConf.setExtraActions(config.getExtraActions());
        } else {
            mergedConf.setExtraActions(new ArrayList<>());
        }
        baseConf.getActions().forEach(fixedAction -> {
            int idx = config.getActions().indexOf(fixedAction);
            if (idx != -1) {
                ConfiguredAction tempAction = config.getActions().get(idx);
                fixedAction.setParams(tempAction.getParams());
                fixedAction.setQuycaId(tempAction.getQuycaId());
                fixedAction.setParams(tempAction.getParams());
            }
            mergedConf.getActions().add(fixedAction);
        });

        baseConf.getEmotions().forEach(fixedEmotion -> {
            int idx = config.getEmotions().indexOf(fixedEmotion);
            if (idx != -1) {
                ConfiguredEmotion tempEmotion = config.getEmotions().get(idx);
                fixedEmotion.setIntensity(tempEmotion.getIntensity());
            }
            mergedConf.getEmotions().add(fixedEmotion);
        });
        return mergedConf;

    }

    /**
     * Parse json quyca configuration.
     *
     * @param response the response
     * @return the quyca configuration
     */
    public static QuycaConfiguration parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(response, QuycaConfiguration.class);
    }

    /**
     * To json string.
     *
     * @param response the response
     * @return the string
     */
    public static String toJSON(QuycaConfiguration response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonBuilder.create();
        return gson.toJson(response);
    }

    @NonNull
    public static QuycaConfiguration getBasicConfig() {
        QuycaConfiguration conf = new QuycaConfiguration();
        List<String> defaultParams = new ArrayList<>();
        defaultParams.add("perc_nominal_vel");

        conf.getActions().add(new ConfiguredAction(FixedConfiguredAction.reverse, "Reversa", new ArrayList<>(defaultParams)));
        conf.getActions().add(new ConfiguredAction(FixedConfiguredAction.forward, "Avanzar", new ArrayList<>(defaultParams)));
        conf.getActions().add(new ConfiguredAction(FixedConfiguredAction.right, "Girar a la Derecha", new ArrayList<>(defaultParams)));
        conf.getActions().add(new ConfiguredAction(FixedConfiguredAction.left, "Girar a la Izquierda", new ArrayList<>(defaultParams)));
        conf.getActions().add(new ConfiguredAction(FixedConfiguredAction.emotions, "Cambiar Emocion", new ArrayList<>()));


        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.happy, "Feliz", 0.5f));
        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.sad, "Triste", -0.5f));
        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.veryhappy, "Muy Feliz", 1f));
        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.verysad, "Muy Triste", -1f));
        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.sick, "Enfermo", 0));
        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.angry, "Furioso", 0.3f));
        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.neutral, "Neutro", 0.5f));
        conf.getEmotions().add(new ConfiguredEmotion(FixedConfiguredEmotion.surprised, "Sorprendido", 0));
        conf.setExtraActions(new ArrayList<>());
        return conf;
    }

    /**
     * Gets emotions.
     *
     * @return emotions emotions
     */
    public List<ConfiguredEmotion> getEmotions() {
        return emotions;
    }

    /**
     * Sets emotions.
     *
     * @param emotions the emotions
     */
    public void setEmotions(List<ConfiguredEmotion> emotions) {
        this.emotions = emotions;
    }

    /**
     * Gets emotions from id.
     *
     * @param emotionId the emotion id
     * @return emotions from id
     */
    public ConfiguredEmotion getEmotionsFromId(FixedConfiguredEmotion emotionId) {
        for (ConfiguredEmotion ce : emotions) {
            if (ce.getEmotionId().equals(emotionId)) {
                return ce;
            }
        }
        return null;
    }

    /**
     * Gets actions.
     *
     * @return the actions
     */
    public List<ConfiguredAction> getActions() {
        return actions;
    }

    /**
     * Sets actions.
     *
     * @param actions the actions
     */
    public void setActions(List<ConfiguredAction> actions) {
        this.actions = actions;
    }

    /**
     * Gets actions from id.
     *
     * @param actionId the action id
     * @return the actions from id
     */
    public ConfiguredAction getActionsFromId(String actionId) {
        for (ConfiguredAction ca : actions) {
            if (ca.getActionId().equals(actionId)) {
                return ca;
            }
        }

        for (ConfiguredAction ca : extraActions) {
            if (ca.getActionId().equals(actionId)) {
                return ca;
            }
        }
        return null;
    }

    public List<ConfiguredAction> getExtraActions() {
        return extraActions;
    }

    public void setExtraActions(List<ConfiguredAction> extraActions) {
        this.extraActions = extraActions;
    }

    @NonNull
    @Override
    public String toString() {
        return "QuycaConfiguration{" +
                "emotions=" + emotions.size() +
                ", actions=" + actions.size() +
                '}';
    }
}
