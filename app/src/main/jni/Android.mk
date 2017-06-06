LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	C:\Users\admin\Desktop\JH03-15\jni\Port\app\src\main\jni\com_jh_port_SerialPort.c \

LOCAL_C_INCLUDES += C:\Users\admin\Desktop\JH03-15\jni\Port\app\src\main\jni
LOCAL_C_INCLUDES += C:\Users\admin\Desktop\JH03-15\jni\Port\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
