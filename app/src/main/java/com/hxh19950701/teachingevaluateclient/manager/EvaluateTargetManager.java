package com.hxh19950701.teachingevaluateclient.manager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hxh19950701.teachingevaluateclient.base.ResponseData;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateFirstTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateSecondTarget;
import com.hxh19950701.teachingevaluateclient.bean.service.EvaluateThirdTarget;
import com.hxh19950701.teachingevaluateclient.interfaces.ManagerInitializeListener;
import com.hxh19950701.teachingevaluateclient.network.api.EvaluateApi;
import com.hxh19950701.teachingevaluateclient.utils.GsonUtils;
import com.hxh19950701.teachingevaluateclient.utils.IdRecordUtils;
import com.hxh19950701.teachingevaluateclient.utils.ToastUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EvaluateTargetManager {

    private static final String TAG = EvaluateTargetManager.class.getSimpleName();

    private static final String KEY = "4C14A42286964298654B147CFF57EA9E";
    private static final String FILE_NAME = "EvaluateTarget.json";

    private EvaluateTargetManager() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static final ManagerInitializeListener defaultInitializeListener = new ManagerInitializeListener() {

        @Override
        public void onSuccess(boolean fromCache) {
            EvaluateTargetManager.printAllTargets();
        }

        @Override
        public void onFailure(Exception initException, Exception updateException) {
            initException.printStackTrace();
            updateException.printStackTrace();
            ToastUtils.show("更新评价条目失败，软件可能工作不正常");
        }

    };

    private static final Initializer INITIALIZER = new Initializer();
    private static final List<EvaluateFirstTarget> FIRST_TARGETS = new ArrayList<>(4);
    private static final List<EvaluateSecondTarget> SECOND_TARGETS = new ArrayList<>(20);
    private static final List<EvaluateThirdTarget> THIRD_TARGETS = new ArrayList<>(50);

    private static ManagerInitializeListener initializeListener = null;

    private static class Initializer extends Thread implements ManagerInitializeListener {

        private static final Handler HANDLER = new Handler();
        private static final Type TYPE = new TypeToken<ResponseData<List<EvaluateThirdTarget>>>() {
        }.getType();

        private Context context = null;

        @Override
        public void run() {
            super.run();
            init();
        }

        public void init() {
            Exception initException = initFromLocal();
            Exception updateException = update();
            if (updateException == null) {
                onSuccess(false);
            } else if (initException == null && updateException != null) {
                onSuccess(true);
            } else {
                onFailure(initException, updateException);
            }
        }

        private Exception update() {
            try {
                String jsonString = EvaluateApi.getAllTargetsSync().readString();
                ResponseData<List<EvaluateThirdTarget>> response = GsonUtils.fromJson(jsonString, TYPE);
                Log.i(TAG, jsonString);
                if (response.getData() == null) {
                    return null;
                } else {
                    initData(response.getData());
                    try {
                        saveJsonToLocal(jsonString);                                        //缓存到本地，缓存失败的话，版本号也不会变
                        //PrefUtils.putString(Constant.KEY_AREA_UP_KEY, areaData.data.upkey); //更新版本号
                    } catch (IOException e) {
                        Log.e(TAG, "已更新到最新，但是缓存失败");
                    }
                    return null;
                }
            } catch (Exception e) {
                return e;
            }
        }

        private void initData(List<EvaluateThirdTarget> data) {
            FIRST_TARGETS.clear();
            SECOND_TARGETS.clear();
            THIRD_TARGETS.clear();
            for (EvaluateThirdTarget thirdTarget : data) {
                THIRD_TARGETS.add(thirdTarget);

                int secondTargetId = thirdTarget.getSecondTarget().getId();
                EvaluateSecondTarget secondTarget = getSecondTargetById(secondTargetId);
                if (secondTarget == null) {
                    secondTarget = thirdTarget.getSecondTarget();
                    secondTarget.setThirdTargets(new ArrayList<EvaluateThirdTarget>(7));
                    SECOND_TARGETS.add(secondTarget);
                }
                secondTarget.getThirdTargets().add(thirdTarget);
                thirdTarget.setSecondTarget(secondTarget);

                int firstTargetId = thirdTarget.getSecondTarget().getFirstTarget().getId();
                EvaluateFirstTarget firstTarget = getFirstTargetById(firstTargetId);
                if (firstTarget == null) {
                    firstTarget = thirdTarget.getSecondTarget().getFirstTarget();
                    firstTarget.setSecondTargets(new ArrayList<EvaluateSecondTarget>(15));
                    FIRST_TARGETS.add(firstTarget);
                }
                if (IdRecordUtils.findIdRecord(firstTarget.getSecondTargets(), secondTargetId) == null) {
                    firstTarget.getSecondTargets().add(secondTarget);
                    secondTarget.setFirstTarget(firstTarget);
                }
            }
        }

        public Exception initFromLocal() {
            try {
                String jsonString = getLocalJson();
                ResponseData<List<EvaluateThirdTarget>> response = GsonUtils.fromJson(jsonString, TYPE);
                initData(response.getData());
                return null;
            } catch (Exception e) {
                return e;
            }
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

        @Override
        public void onFailure(final Exception initException, final Exception updateException) {
            if (initializeListener != null) {
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        initializeListener.onFailure(initException, updateException);
                    }
                });
            }
        }
    }

    public static void init(Context context) {
        if (!INITIALIZER.isAlive()) {
            INITIALIZER.context = context;
            INITIALIZER.start();
        } else {
            Log.w(TAG, "正在进行初始化，此次初始化请求将被忽略。");
        }
    }

    public static void setInitializeListener(ManagerInitializeListener initializeListener) {
        EvaluateTargetManager.initializeListener = initializeListener;
    }

    public static ManagerInitializeListener getDefaultInitializeListener() {
        return defaultInitializeListener;
    }

    public static EvaluateThirdTarget getThirdTargetById(int id) {
        return (EvaluateThirdTarget) IdRecordUtils.findIdRecord(THIRD_TARGETS, id);
    }

    public static EvaluateSecondTarget getSecondTargetById(int id) {
        return (EvaluateSecondTarget) IdRecordUtils.findIdRecord(SECOND_TARGETS, id);
    }

    public static EvaluateFirstTarget getFirstTargetById(int id) {
        return (EvaluateFirstTarget) IdRecordUtils.findIdRecord(FIRST_TARGETS, id);
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

    public static void printAllTargets() {
        for (EvaluateFirstTarget firstTarget : FIRST_TARGETS) {
            Log.d(TAG, firstTarget.getName());
            for (EvaluateSecondTarget secondTarget : firstTarget.getSecondTargets()) {
                Log.d(TAG, " " + secondTarget.getName());
                for (EvaluateThirdTarget thirdTarget : secondTarget.getThirdTargets()) {
                    Log.d(TAG, "  " + thirdTarget.getName());
                }
            }
        }
    }
}