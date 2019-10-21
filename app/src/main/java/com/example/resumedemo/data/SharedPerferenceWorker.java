package com.example.resumedemo.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SharedPerferenceWorker {
    private static SharedPerferenceWorker dataManager;
    private static Context mContext;

    public static SharedPerferenceWorker getInstance(Context context) {
        if (dataManager == null) {
            dataManager = new SharedPerferenceWorker();
        }
        mContext = context;
        return dataManager;
    }

    public void setData(String key, Object data) {
        if (mContext != null ) {
            try {
                SharedPreferences dataPreferences = mContext.getSharedPreferences("user_defult", Activity.MODE_PRIVATE);
                if (data != null) {
                    if (data instanceof String) {
                        dataPreferences.edit().putString(key, (String) data).apply();
                    } else if (data instanceof Integer) {
                        dataPreferences.edit().putInt(key, (Integer) data).apply();
                    } else if (data instanceof Long) {
                        dataPreferences.edit().putLong(key, (Long) data).apply();
                    } else if (data instanceof Boolean) {
                        dataPreferences.edit().putBoolean(key, (Boolean) data).apply();
                    } else if (data instanceof Float) {
                        dataPreferences.edit().putFloat(key, (Float) data).apply();
                    } else if (data instanceof Set) {
                        dataPreferences.edit().putStringSet(key, (Set<String>) data).apply();
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public Object getData(String key) {
        if (mContext != null) {
            try {
                SharedPreferences dataPreferences = mContext.getSharedPreferences("user_defult", Activity.MODE_PRIVATE);
                boolean isInPreferences = dataPreferences.contains(key);
                Object value = new Object();
                if (isInPreferences) {
                    Map<String, ?> allValues = dataPreferences.getAll();
                    for (Map.Entry<String, ?> entry : allValues.entrySet()) {
                        if (entry.getKey().equals(key)) {
                            value = entry.getValue();
                        }
                    }
                    return value;
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return null;
    }

    public void removeData(String key) {
        if (mContext != null) {
            try {
                SharedPreferences dataPreferences = mContext.getSharedPreferences("user_defult", Activity.MODE_PRIVATE);
                dataPreferences.edit().remove(key).apply();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
}
