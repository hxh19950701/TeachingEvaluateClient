package com.hxh19950701.teachingevaluateclient.manager;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateSecondTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.IdRecord;
import com.hxh19950701.teachingevaluateclient.internet.api.EvaluateApi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EvaluateTargetManager {

    private static final String KEY = "4C14A42286964298654B147CFF57EA9E";
    private static final String FILE_NAME = "EvaluateTarget.json";

    private EvaluateTargetManager() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public interface InitializeListener {
        void onSuccess(boolean fromCache);

        void onFailure(Exception e);
    }

    private static final Initializer INITIALIZER = new Initializer();
    private static final List<EvaluateFirstTarget> FIRST_TARGETS = new ArrayList<>(4);
    private static final List<EvaluateSecondTarget> SECOND_TARGETS = new ArrayList<>(20);
    private static final List<EvaluateThirdTarget> THIRD_TARGETS = new ArrayList<>(50);

    private static InitializeListener initializeListener = null;

    private static class Initializer extends Thread implements InitializeListener {

        private static final Handler HANDLER = new Handler();
        private static final Type TYPE = new TypeToken<ResponseData<List<EvaluateThirdTarget>>>() {}.getType();
        private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        private Context context = null;

        @Override
        public void run() {
            super.run();
            init();
        }

        public void init() {
            initFromServer(true);
        }

        public void initFromServer(boolean allowCache) {
            try {
                String jsonString = EvaluateApi.getAllTargetsSync().readString();
                ResponseData<List<EvaluateThirdTarget>> response = GSON.fromJson(jsonString, TYPE);
                if (response.getData() == null) {
                    initFromLocal();                                                        //数据已是最新，使用缓存
                } else {
                    initData(response.getData());
                    try {
                        saveJsonToLocal(jsonString);                                        //缓存到本地，缓存失败的话，版本号也不会变
                        //PrefUtils.putString(Constant.KEY_AREA_UP_KEY, areaData.data.upkey); //更新版本号
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                onSuccess(false);
            } catch (Exception e) {
                e.printStackTrace();
                if (allowCache) {
                    try {
                        initFromLocal();
                        onSuccess(true);
                    } catch (Exception e1) {
                        onFailure(e1);
                    }
                } else {
                    onFailure(e);
                }
            }
        }

        private void initData(List<EvaluateThirdTarget> data) {
            FIRST_TARGETS.clear();
            SECOND_TARGETS.clear();
            THIRD_TARGETS.clear();
            for (EvaluateThirdTarget thirdTarget : data) {
                EvaluateSecondTarget secondTarget = thirdTarget.getSecondTarget();
                EvaluateFirstTarget firstTarget = secondTarget.getFirstTarget();
                secondTarget.getThirdTargets().add(thirdTarget);
                firstTarget.getSecondTargets().add(secondTarget);
                if (getFirstTargetById(firstTarget.getId()) == null) {
                    FIRST_TARGETS.add(firstTarget);
                }
                if (getSecondTargetById(secondTarget.getId()) == null) {
                    SECOND_TARGETS.add(secondTarget);
                }
                THIRD_TARGETS.add(thirdTarget);
            }
        }

        public void initFromLocal() throws Exception {
            String jsonString = getLocalJson();
            ResponseData<List<EvaluateThirdTarget>> response = GSON.fromJson(jsonString, TYPE);
            initData(response.getData());
        }

        private void saveJsonToLocal(String jsonString) throws IOException {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            byte[] bytes = jsonString.getBytes();
            fos.write(bytes);
            fos.close();
        }

        private String getLocalJson() throws IOException {
            FileInputStream in = context.openFileInput(FILE_NAME);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            return new String(buffer);
        }

        public void onSuccess(final boolean fromCache) {
            if (initializeListener != null) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        initializeListener.onSuccess(fromCache);
                    }
                });
            }
        }

        public void onFailure(final Exception e) {
            if (initializeListener != null) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        initializeListener.onFailure(e);
                    }
                });
            }
        }
    }

    public static void init(Context context) {
        INITIALIZER.context = context;
        INITIALIZER.start();
    }

    public static void setInitializeListener(InitializeListener initializeListener) {
        EvaluateTargetManager.initializeListener = initializeListener;
    }

    private static final IdRecord findTarget(List<? extends IdRecord> data, int id) {
        if (id < 0) return null;
        for (IdRecord item : data) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public static EvaluateThirdTarget getThirdTargetById(int id) {
        return (EvaluateThirdTarget) findTarget(THIRD_TARGETS, id);
    }

    public static EvaluateSecondTarget getSecondTargetById(int id) {
        return (EvaluateSecondTarget) findTarget(SECOND_TARGETS, id);
    }

    public static EvaluateFirstTarget getFirstTargetById(int id) {
        return (EvaluateFirstTarget) findTarget(THIRD_TARGETS, id);
    }

    public static List<EvaluateFirstTarget> getFirstTargets() {
        return FIRST_TARGETS;
    }

    public static List<EvaluateSecondTarget> getSecondTargets() {
        return SECOND_TARGETS;
    }

    public static List<EvaluateThirdTarget> getThirdTargets() {
        return THIRD_TARGETS;
    }
}