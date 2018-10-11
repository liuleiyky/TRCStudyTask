//package com.liul.trc_study_task;
//
//import android.app.backup.BackupAgent;
//import android.content.BroadcastReceiver;
//import android.content.ComponentCallbacks;
//import android.content.ComponentName;
//import android.content.ContentProvider;
//import android.content.Context;
//import android.content.IContentProvider;
//import android.content.Intent;
//import android.content.IIntentReceiver;
//import android.content.pm.ActivityInfo;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.IPackageManager;
//import android.content.pm.InstrumentationInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.ProviderInfo;
//import android.content.pm.ServiceInfo;
//import android.content.res.AssetManager;
//import android.content.res.CompatibilityInfo;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDebug;
//import android.database.sqlite.SQLiteDebug.DbStats;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Debug;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.os.MessageQueue;
//import android.os.ParcelFileDescriptor;
//import android.os.Process;
//import android.os.RemoteException;
//import android.os.ServiceManager;
//import android.os.StrictMode;
//import android.os.SystemClock;
//import android.util.AndroidRuntimeException;
//import android.util.Config;
//import android.util.DisplayMetrics;
//import android.util.EventLog;
//import android.util.Log;
//import android.util.LogPrinter;
//import android.util.Slog;
//import android.view.Display;
//import android.view.View;
//import android.view.ViewDebug;
//import android.view.ViewManager;
//import android.view.ViewRoot;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.WindowManagerImpl;
//
//import com.android.internal.os.BinderInternal;
//import com.android.internal.os.RuntimeInit;
//import com.android.internal.os.SamplingProfilerIntegration;
//
//import org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl;
//
//import java.io.File;
//import java.io.FileDescriptor;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.TimeZone;
//import java.util.regex.Pattern;
//
//import dalvik.system.SamplingProfiler;
//
//final class SuperNotCalledException extends AndroidRuntimeException {
//    public SuperNotCalledException(String msg) {
//        super(msg);
//    }
//}
//
//final class RemoteServiceException extends AndroidRuntimeException {
//    public RemoteServiceException(String msg) {
//        super(msg);
//    }
//}
//
///**
// * This manages the execution of the main thread in an
// * application process, scheduling and executing activities,
// * broadcasts, and other operations on it as the activity
// * manager requests.
// * <p>
// * {@hide}
// */
//public final class ActivityThread {
//    static final String TAG = "ActivityThread";
//    private static final android.graphics.Bitmap.Config THUMBNAIL_FORMAT = Bitmap.Config.RGB_565;
//    private static final boolean DEBUG = false;
//    static final boolean localLOGV = DEBUG ? Config.LOGD : Config.LOGV;
//    static final boolean DEBUG_MESSAGES = false;
//    static final boolean DEBUG_BROADCAST = false;
//    private static final boolean DEBUG_RESULTS = false;
//    private static final boolean DEBUG_BACKUP = false;
//    private static final boolean DEBUG_CONFIGURATION = false;
//    private static final long MIN_TIME_BETWEEN_GCS = 5 * 1000;
//    private static final Pattern PATTERN_SEMICOLON = Pattern.compile(";");
//    private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;
//    private static final int LOG_ON_PAUSE_CALLED = 30021;
//    private static final int LOG_ON_RESUME_CALLED = 30022;
//
//    static ContextImpl mSystemContext = null;
//
//    static IPackageManager sPackageManager;
//
//    final ApplicationThread mAppThread = new ApplicationThread();
//    final Looper mLooper = Looper.myLooper();
//    final H mH = new H();
//    final HashMap<ibinder, activityclientrecord="">mActivities
//            =new HashMap<ibinder, activityclientrecord="">();
//    // List of new activities (via ActivityRecord.nextIdle) that should
//    // be reported when next we idle.
//    ActivityClientRecord mNewActivities = null;
//    // Number of activities that are currently visible on-screen.
//    int mNumVisibleActivities = 0;
//    final HashMap<ibinder, service="">mServices
//            =new HashMap<ibinder, service="">();
//    AppBindData mBoundApplication;
//    Configuration mConfiguration;
//    Configuration mResConfiguration;
//    Application mInitialApplication;
//    final ArrayList mAllApplications
//            = new ArrayList();
//    // set of instantiated backup agents, keyed by package name
//    final HashMap<string, backupagent="">mBackupAgents =new HashMap<string, backupagent="">();
//    static final ThreadLocal sThreadLocal = new ThreadLocal();
//    Instrumentation mInstrumentation;
//    String mInstrumentationAppDir = null;
//    String mInstrumentationAppPackage = null;
//    String mInstrumentedAppDir = null;
//    boolean mSystemThread = false;
//    boolean mJitEnabled = false;
//
//    // These can be accessed by multiple threads; mPackages is the lock.
//    // XXX For now we keep around information about all packages we have
//    // seen, not removing entries from this map.
//    final HashMap<string, loadedapk="">>mPackages
//            =new HashMap<string, loadedapk="">>();
//    final HashMap<string, loadedapk="">>mResourcePackages
//            =new HashMap<string, loadedapk="">>();
//    Display mDisplay = null;
//    DisplayMetrics mDisplayMetrics = null;
//    final HashMap<resourceskey, resources="">>mActiveResources
//            =new HashMap<resourceskey, resources="">>();
//    final ArrayList mRelaunchingActivities
//            = new ArrayList();
//    Configuration mPendingConfiguration = null;
//
//    // The lock of mProviderMap protects the following variables.
//    final HashMap<string, providerclientrecord="">mProviderMap
//        =new HashMap<string, providerclientrecord="">();
//    final HashMap<ibinder, providerrefcount="">mProviderRefCountMap
//        =new HashMap<ibinder, providerrefcount="">();
//    final HashMap<ibinder, providerclientrecord="">mLocalProviders
//        =new HashMap<ibinder, providerclientrecord="">();
//
//    final GcIdler mGcIdler = new GcIdler();
//    boolean mGcIdlerScheduled = false;
//
//    static Handler sMainThreadHandler;  // set once in main()
//
//    private static final class ActivityClientRecord {
//        IBinder token;
//        int ident;
//        Intent intent;
//        Bundle state;
//        Activity activity;
//        Window window;
//        Activity parent;
//        String embeddedID;
//        Object lastNonConfigurationInstance;
//        HashMap<string, object> lastNonConfigurationChildInstances;
//        boolean paused;
//        boolean stopped;
//        boolean hideForNow;
//        Configuration newConfig;
//        Configuration createdConfig;
//        ActivityClientRecord nextIdle;
//
//        ActivityInfo activityInfo;
//        LoadedApk packageInfo;
//
//        List<resultinfo> pendingResults;
//        List<intent> pendingIntents;
//
//        boolean startsNotResumed;
//        boolean isForward;
//
//        ActivityClientRecord() {
//            parent = null;
//            embeddedID = null;
//            paused = false;
//            stopped = false;
//            hideForNow = false;
//            nextIdle = null;
//        }
//
//        public String toString() {
//            ComponentName componentName = intent.getComponent();
//            return "ActivityRecord{"
//                    + Integer.toHexString(System.identityHashCode(this))
//                    + " token=" + token + " " + (componentName == null
//                    ? "no component name" : componentName.toShortString())
//                    + "}";
//        }
//    }
//
//    private final class ProviderClientRecord implements IBinder.DeathRecipient {
//        final String mName;
//        final IContentProvider mProvider;
//        final ContentProvider mLocalProvider;
//
//        ProviderClientRecord(String name, IContentProvider provider,
//                             ContentProvider localProvider) {
//            mName = name;
//            mProvider = provider;
//            mLocalProvider = localProvider;
//        }
//
//        public void binderDied() {
//            removeDeadProvider(mName, mProvider);
//        }
//    }
//
//    private static final class NewIntentData {
//        List<intent> intents;
//        IBinder token;
//
//        public String toString() {
//            return "NewIntentData{intents=" + intents + " token=" + token + "}";
//        }
//    }
//
//    private static final class ReceiverData {
//        Intent intent;
//        ActivityInfo info;
//        int resultCode;
//        String resultData;
//        Bundle resultExtras;
//        boolean sync;
//        boolean resultAbort;
//
//        public String toString() {
//            return "ReceiverData{intent=" + intent + " packageName=" +
//                    info.packageName + " resultCode=" + resultCode
//                    + " resultData=" + resultData + " resultExtras=" + resultExtras + "}";
//        }
//    }
//
//    private static final class CreateBackupAgentData {
//        ApplicationInfo appInfo;
//        int backupMode;
//
//        public String toString() {
//            return "CreateBackupAgentData{appInfo=" + appInfo
//                    + " backupAgent=" + appInfo.backupAgentName
//                    + " mode=" + backupMode + "}";
//        }
//    }
//
//    private static final class CreateServiceData {
//        IBinder token;
//        ServiceInfo info;
//        Intent intent;
//
//        public String toString() {
//            return "CreateServiceData{token=" + token + " className="
//                    + info.name + " packageName=" + info.packageName
//                    + " intent=" + intent + "}";
//        }
//    }
//
//    private static final class BindServiceData {
//        IBinder token;
//        Intent intent;
//        boolean rebind;
//
//        public String toString() {
//            return "BindServiceData{token=" + token + " intent=" + intent + "}";
//        }
//    }
//
//    private static final class ServiceArgsData {
//        IBinder token;
//        int startId;
//        int flags;
//        Intent args;
//
//        public String toString() {
//            return "ServiceArgsData{token=" + token + " startId=" + startId
//                    + " args=" + args + "}";
//        }
//    }
//
//    private static final class AppBindData {
//        LoadedApk info;
//        String processName;
//        ApplicationInfo appInfo;
//        List<providerinfo> providers;
//        ComponentName instrumentationName;
//        String profileFile;
//        Bundle instrumentationArgs;
//        IInstrumentationWatcher instrumentationWatcher;
//        int debugMode;
//        boolean restrictedBackupMode;
//        Configuration config;
//        boolean handlingProfiling;
//
//        public String toString() {
//            return "AppBindData{appInfo=" + appInfo + "}";
//        }
//    }
//
//    private static final class DumpServiceInfo {
//        FileDescriptor fd;
//        IBinder service;
//        String[] args;
//        boolean dumped;
//    }
//
//    private static final class ResultData {
//        IBinder token;
//        List<resultinfo> results;
//
//        public String toString() {
//            return "ResultData{token=" + token + " results" + results + "}";
//        }
//    }
//
//    private static final class ContextCleanupInfo {
//        ContextImpl context;
//        String what;
//        String who;
//    }
//
//    private static final class ProfilerControlData {
//        String path;
//        ParcelFileDescriptor fd;
//    }
//
//    private final class ApplicationThread extends ApplicationThreadNative {
//        private static final String HEAP_COLUMN = "%17s %8s %8s %8s %8s";
//        private static final String ONE_COUNT_COLUMN = "%17s %8d";
//        private static final String TWO_COUNT_COLUMNS = "%17s %8d %17s %8d";
//        private static final String TWO_COUNT_COLUMNS_DB = "%20s %8d %20s %8d";
//        private static final String DB_INFO_FORMAT = "  %8d %8d %14d  %s";
//
//        // Formatting for checkin service - update version if row format changes
//        private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 1;
//
//        public final void schedulePauseActivity(IBinder token, boolean finished,
//                                                boolean userLeaving, int configChanges) {
//            queueOrSendMessage(
//                    finished ? H.PAUSE_ACTIVITY_FINISHING : H.PAUSE_ACTIVITY,
//                    token,
//                    (userLeaving ? 1 : 0),
//                    configChanges);
//        }
//
//        public final void scheduleStopActivity(IBinder token, boolean showWindow,
//                                               int configChanges) {
//            queueOrSendMessage(
//                    showWindow ? H.STOP_ACTIVITY_SHOW : H.STOP_ACTIVITY_HIDE,
//                    token, 0, configChanges);
//        }
//
//        public final void scheduleWindowVisibility(IBinder token, boolean showWindow) {
//            queueOrSendMessage(
//                    showWindow ? H.SHOW_WINDOW : H.HIDE_WINDOW,
//                    token);
//        }
//
//        public final void scheduleResumeActivity(IBinder token, boolean isForward) {
//            queueOrSendMessage(H.RESUME_ACTIVITY, token, isForward ? 1 : 0);
//        }
//
//        public final void scheduleSendResult(IBinder token, List<resultinfo> results) {
//            ResultData res = new ResultData();
//            res.token = token;
//            res.results = results;
//            queueOrSendMessage(H.SEND_RESULT, res);
//        }
//
//        // we use token to identify this activity without having to send the
//        // activity itself back to the activity manager. (matters more with ipc)
//        public final void scheduleLaunchActivity(Intent intent, IBinder token, int ident,
//                                                 ActivityInfo info, Bundle state, List<resultinfo> pendingResults,
//                                                 List<intent> pendingNewIntents, boolean notResumed, boolean isForward) {
//            ActivityClientRecord r = new ActivityClientRecord();
//
//            r.token = token;
//            r.ident = ident;
//            r.intent = intent;
//            r.activityInfo = info;
//            r.state = state;
//
//            r.pendingResults = pendingResults;
//            r.pendingIntents = pendingNewIntents;
//
//            r.startsNotResumed = notResumed;
//            r.isForward = isForward;
//
//            queueOrSendMessage(H.LAUNCH_ACTIVITY, r);
//        }
//
//        public final void scheduleRelaunchActivity(IBinder token,
//                                                   List<resultinfo> pendingResults, List<intent> pendingNewIntents,
//                                                   int configChanges, boolean notResumed, Configuration config) {
//            ActivityClientRecord r = new ActivityClientRecord();
//
//            r.token = token;
//            r.pendingResults = pendingResults;
//            r.pendingIntents = pendingNewIntents;
//            r.startsNotResumed = notResumed;
//            r.createdConfig = config;
//
//            synchronized (mPackages) {
//                mRelaunchingActivities.add(r);
//            }
//
//            queueOrSendMessage(H.RELAUNCH_ACTIVITY, r, configChanges);
//        }
//
//        public final void scheduleNewIntent(List<intent> intents, IBinder token) {
//            NewIntentData data = new NewIntentData();
//            data.intents = intents;
//            data.token = token;
//
//            queueOrSendMessage(H.NEW_INTENT, data);
//        }
//
//        public final void scheduleDestroyActivity(IBinder token, boolean finishing,
//                                                  int configChanges) {
//            queueOrSendMessage(H.DESTROY_ACTIVITY, token, finishing ? 1 : 0,
//                    configChanges);
//        }
//
//        public final void scheduleReceiver(Intent intent, ActivityInfo info,
//                                           int resultCode, String data, Bundle extras, boolean sync) {
//            ReceiverData r = new ReceiverData();
//
//            r.intent = intent;
//            r.info = info;
//            r.resultCode = resultCode;
//            r.resultData = data;
//            r.resultExtras = extras;
//            r.sync = sync;
//
//            queueOrSendMessage(H.RECEIVER, r);
//        }
//
//        public final void scheduleCreateBackupAgent(ApplicationInfo app, int backupMode) {
//            CreateBackupAgentData d = new CreateBackupAgentData();
//            d.appInfo = app;
//            d.backupMode = backupMode;
//
//            queueOrSendMessage(H.CREATE_BACKUP_AGENT, d);
//        }
//
//        public final void scheduleDestroyBackupAgent(ApplicationInfo app) {
//            CreateBackupAgentData d = new CreateBackupAgentData();
//            d.appInfo = app;
//
//            queueOrSendMessage(H.DESTROY_BACKUP_AGENT, d);
//        }
//
//        public final void scheduleCreateService(IBinder token,
//                                                ServiceInfo info) {
//            CreateServiceData s = new CreateServiceData();
//            s.token = token;
//            s.info = info;
//
//            queueOrSendMessage(H.CREATE_SERVICE, s);
//        }
//
//        public final void scheduleBindService(IBinder token, Intent intent,
//                                              boolean rebind) {
//            BindServiceData s = new BindServiceData();
//            s.token = token;
//            s.intent = intent;
//            s.rebind = rebind;
//
//            queueOrSendMessage(H.BIND_SERVICE, s);
//        }
//
//        public final void scheduleUnbindService(IBinder token, Intent intent) {
//            BindServiceData s = new BindServiceData();
//            s.token = token;
//            s.intent = intent;
//
//            queueOrSendMessage(H.UNBIND_SERVICE, s);
//        }
//
//        public final void scheduleServiceArgs(IBinder token, int startId,
//                                              int flags, Intent args) {
//            ServiceArgsData s = new ServiceArgsData();
//            s.token = token;
//            s.startId = startId;
//            s.flags = flags;
//            s.args = args;
//
//            queueOrSendMessage(H.SERVICE_ARGS, s);
//        }
//
//        public final void scheduleStopService(IBinder token) {
//            queueOrSendMessage(H.STOP_SERVICE, token);
//        }
//
//        public final void bindApplication(String processName,
//                                          ApplicationInfo appInfo, List<providerinfo> providers,
//                                          ComponentName instrumentationName, String profileFile,
//                                          Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher,
//                                          int debugMode, boolean isRestrictedBackupMode, Configuration config,
//                                          Map<string, ibinder="">services) {
//
//            if (services != null) {
//                // Setup the service cache in the ServiceManager
//                ServiceManager.initServiceCache(services);
//            }
//
//            AppBindData data = new AppBindData();
//            data.processName = processName;
//            data.appInfo = appInfo;
//            data.providers = providers;
//            data.instrumentationName = instrumentationName;
//            data.profileFile = profileFile;
//            data.instrumentationArgs = instrumentationArgs;
//            data.instrumentationWatcher = instrumentationWatcher;
//            data.debugMode = debugMode;
//            data.restrictedBackupMode = isRestrictedBackupMode;
//            data.config = config;
//            queueOrSendMessage(H.BIND_APPLICATION, data);
//        }
//
//        public final void scheduleExit() {
//            queueOrSendMessage(H.EXIT_APPLICATION, null);
//        }
//
//        public final void scheduleSuicide() {
//            queueOrSendMessage(H.SUICIDE, null);
//        }
//
//        public void requestThumbnail(IBinder token) {
//            queueOrSendMessage(H.REQUEST_THUMBNAIL, token);
//        }
//
//        public void scheduleConfigurationChanged(Configuration config) {
//            synchronized (mPackages) {
//                if (mPendingConfiguration == null ||
//                        mPendingConfiguration.isOtherSeqNewer(config)) {
//                    mPendingConfiguration = config;
//                }
//            }
//            queueOrSendMessage(H.CONFIGURATION_CHANGED, config);
//        }
//
//        public void updateTimeZone() {
//            TimeZone.setDefault(null);
//        }
//
//        public void processInBackground() {
//            mH.removeMessages(H.GC_WHEN_IDLE);
//            mH.sendMessage(mH.obtainMessage(H.GC_WHEN_IDLE));
//        }
//
//        public void dumpService(FileDescriptor fd, IBinder servicetoken, String[] args) {
//            DumpServiceInfo data = new DumpServiceInfo();
//            data.fd = fd;
//            data.service = servicetoken;
//            data.args = args;
//            data.dumped = false;
//            queueOrSendMessage(H.DUMP_SERVICE, data);
//            synchronized (data) {
//                while (!data.dumped) {
//                    try {
//                        data.wait();
//                    } catch (InterruptedException e) {
//                        // no need to do anything here, we will keep waiting until
//                        // dumped is set
//                    }
//                }
//            }
//        }
//
//        // This function exists to make sure all receiver dispatching is
//        // correctly ordered, since these are one-way calls and the binder driver
//        // applies transaction ordering per object for such calls.
//        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent,
//                                               int resultCode, String dataStr, Bundle extras, boolean ordered,
//                                               boolean sticky) throws RemoteException {
//            receiver.performReceive(intent, resultCode, dataStr, extras, ordered, sticky);
//        }
//
//        public void scheduleLowMemory() {
//            queueOrSendMessage(H.LOW_MEMORY, null);
//        }
//
//        public void scheduleActivityConfigurationChanged(IBinder token) {
//            queueOrSendMessage(H.ACTIVITY_CONFIGURATION_CHANGED, token);
//        }
//
//        public void profilerControl(boolean start, String path, ParcelFileDescriptor fd) {
//            ProfilerControlData pcd = new ProfilerControlData();
//            pcd.path = path;
//            pcd.fd = fd;
//            queueOrSendMessage(H.PROFILER_CONTROL, pcd, start ? 1 : 0);
//        }
//
//        public void setSchedulingGroup(int group) {
//            // Note: do this immediately, since going into the foreground
//            // should happen regardless of what pending work we have to do
//            // and the activity manager will wait for us to report back that
//            // we are done before sending us to the background.
//            try {
//                Process.setProcessGroup(Process.myPid(), group);
//            } catch (Exception e) {
//                Slog.w(TAG, "Failed setting process group to " + group, e);
//            }
//        }
//
//        public void getMemoryInfo(Debug.MemoryInfo outInfo) {
//            Debug.getMemoryInfo(outInfo);
//        }
//
//        public void dispatchPackageBroadcast(int cmd, String[] packages) {
//            queueOrSendMessage(H.DISPATCH_PACKAGE_BROADCAST, packages, cmd);
//        }
//
//        public void scheduleCrash(String msg) {
//            queueOrSendMessage(H.SCHEDULE_CRASH, msg);
//        }
//
//        @Override
//        protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
//            long nativeMax = Debug.getNativeHeapSize() / 1024;
//            long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
//            long nativeFree = Debug.getNativeHeapFreeSize() / 1024;
//
//            Debug.MemoryInfo memInfo = new Debug.MemoryInfo();
//            Debug.getMemoryInfo(memInfo);
//
//            final int nativeShared = memInfo.nativeSharedDirty;
//            final int dalvikShared = memInfo.dalvikSharedDirty;
//            final int otherShared = memInfo.otherSharedDirty;
//
//            final int nativePrivate = memInfo.nativePrivateDirty;
//            final int dalvikPrivate = memInfo.dalvikPrivateDirty;
//            final int otherPrivate = memInfo.otherPrivateDirty;
//
//            Runtime runtime = Runtime.getRuntime();
//
//            long dalvikMax = runtime.totalMemory() / 1024;
//            long dalvikFree = runtime.freeMemory() / 1024;
//            long dalvikAllocated = dalvikMax - dalvikFree;
//            long viewInstanceCount = ViewDebug.getViewInstanceCount();
//            long viewRootInstanceCount = ViewDebug.getViewRootInstanceCount();
//            long appContextInstanceCount = ContextImpl.getInstanceCount();
//            long activityInstanceCount = Activity.getInstanceCount();
//            int globalAssetCount = AssetManager.getGlobalAssetCount();
//            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
//            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
//            int binderProxyObjectCount = Debug.getBinderProxyObjectCount();
//            int binderDeathObjectCount = Debug.getBinderDeathObjectCount();
//            int openSslSocketCount = OpenSSLSocketImpl.getInstanceCount();
//            long sqliteAllocated = SQLiteDebug.getHeapAllocatedSize() / 1024;
//            SQLiteDebug.PagerStats stats = SQLiteDebug.getDatabaseInfo();
//
//            // Check to see if we were called by checkin server. If so, print terse format.
//            boolean doCheckinFormat = false;
//            if (args != null) {
//                for (String arg : args) {
//                    if ("-c".equals(arg)) doCheckinFormat = true;
//                }
//            }
//
//            // For checkin, we print one long comma-separated list of values
//            if (doCheckinFormat) {
//                // NOTE: if you change anything significant below, also consider changing
//                // ACTIVITY_THREAD_CHECKIN_VERSION.
//                String processName = (mBoundApplication != null)
//                        ? mBoundApplication.processName : "unknown";
//
//                // Header
//                pw.print(ACTIVITY_THREAD_CHECKIN_VERSION);
//                pw.print(',');
//                pw.print(Process.myPid());
//                pw.print(',');
//                pw.print(processName);
//                pw.print(',');
//
//                // Heap info - max
//                pw.print(nativeMax);
//                pw.print(',');
//                pw.print(dalvikMax);
//                pw.print(',');
//                pw.print("N/A,");
//                pw.print(nativeMax + dalvikMax);
//                pw.print(',');
//
//                // Heap info - allocated
//                pw.print(nativeAllocated);
//                pw.print(',');
//                pw.print(dalvikAllocated);
//                pw.print(',');
//                pw.print("N/A,");
//                pw.print(nativeAllocated + dalvikAllocated);
//                pw.print(',');
//
//                // Heap info - free
//                pw.print(nativeFree);
//                pw.print(',');
//                pw.print(dalvikFree);
//                pw.print(',');
//                pw.print("N/A,");
//                pw.print(nativeFree + dalvikFree);
//                pw.print(',');
//
//                // Heap info - proportional set size
//                pw.print(memInfo.nativePss);
//                pw.print(',');
//                pw.print(memInfo.dalvikPss);
//                pw.print(',');
//                pw.print(memInfo.otherPss);
//                pw.print(',');
//                pw.print(memInfo.nativePss + memInfo.dalvikPss + memInfo.otherPss);
//                pw.print(',');
//
//                // Heap info - shared
//                pw.print(nativeShared);
//                pw.print(',');
//                pw.print(dalvikShared);
//                pw.print(',');
//                pw.print(otherShared);
//                pw.print(',');
//                pw.print(nativeShared + dalvikShared + otherShared);
//                pw.print(',');
//
//                // Heap info - private
//                pw.print(nativePrivate);
//                pw.print(',');
//                pw.print(dalvikPrivate);
//                pw.print(',');
//                pw.print(otherPrivate);
//                pw.print(',');
//                pw.print(nativePrivate + dalvikPrivate + otherPrivate);
//                pw.print(',');
//
//                // Object counts
//                pw.print(viewInstanceCount);
//                pw.print(',');
//                pw.print(viewRootInstanceCount);
//                pw.print(',');
//                pw.print(appContextInstanceCount);
//                pw.print(',');
//                pw.print(activityInstanceCount);
//                pw.print(',');
//
//                pw.print(globalAssetCount);
//                pw.print(',');
//                pw.print(globalAssetManagerCount);
//                pw.print(',');
//                pw.print(binderLocalObjectCount);
//                pw.print(',');
//                pw.print(binderProxyObjectCount);
//                pw.print(',');
//
//                pw.print(binderDeathObjectCount);
//                pw.print(',');
//                pw.print(openSslSocketCount);
//                pw.print(',');
//
//                // SQL
//                pw.print(sqliteAllocated);
//                pw.print(',');
//                pw.print(stats.memoryUsed / 1024);
//                pw.print(',');
//                pw.print(stats.pageCacheOverflo / 1024);
//                pw.print(',');
//                pw.print(stats.largestMemAlloc / 1024);
//                pw.print(',');
//                for (int i = 0; i < stats.dbStats.size(); i++) {
//                    DbStats dbStats = stats.dbStats.get(i);
//                    printRow(pw, DB_INFO_FORMAT, dbStats.pageSize, dbStats.dbSize,
//                            dbStats.lookaside, dbStats.dbName);
//                    pw.print(',');
//                }
//
//                return;
//            }
//
//            // otherwise, show human-readable format
//            printRow(pw, HEAP_COLUMN, "", "native", "dalvik", "other", "total");
//            printRow(pw, HEAP_COLUMN, "size:", nativeMax, dalvikMax, "N/A", nativeMax + dalvikMax);
//            printRow(pw, HEAP_COLUMN, "allocated:", nativeAllocated, dalvikAllocated, "N/A",
//                    nativeAllocated + dalvikAllocated);
//            printRow(pw, HEAP_COLUMN, "free:", nativeFree, dalvikFree, "N/A",
//                    nativeFree + dalvikFree);
//
//            printRow(pw, HEAP_COLUMN, "(Pss):", memInfo.nativePss, memInfo.dalvikPss,
//                    memInfo.otherPss, memInfo.nativePss + memInfo.dalvikPss + memInfo.otherPss);
//
//            printRow(pw, HEAP_COLUMN, "(shared dirty):", nativeShared, dalvikShared, otherShared,
//                    nativeShared + dalvikShared + otherShared);
//            printRow(pw, HEAP_COLUMN, "(priv dirty):", nativePrivate, dalvikPrivate, otherPrivate,
//                    nativePrivate + dalvikPrivate + otherPrivate);
//
//            pw.println(" ");
//            pw.println(" Objects");
//            printRow(pw, TWO_COUNT_COLUMNS, "Views:", viewInstanceCount, "ViewRoots:",
//                    viewRootInstanceCount);
//
//            printRow(pw, TWO_COUNT_COLUMNS, "AppContexts:", appContextInstanceCount,
//                    "Activities:", activityInstanceCount);
//
//            printRow(pw, TWO_COUNT_COLUMNS, "Assets:", globalAssetCount,
//                    "AssetManagers:", globalAssetManagerCount);
//
//            printRow(pw, TWO_COUNT_COLUMNS, "Local Binders:", binderLocalObjectCount,
//                    "Proxy Binders:", binderProxyObjectCount);
//            printRow(pw, ONE_COUNT_COLUMN, "Death Recipients:", binderDeathObjectCount);
//
//            printRow(pw, ONE_COUNT_COLUMN, "OpenSSL Sockets:", openSslSocketCount);
//
//            // SQLite mem info
//            pw.println(" ");
//            pw.println(" SQL");
//            printRow(pw, TWO_COUNT_COLUMNS_DB, "heap:", sqliteAllocated, "MEMORY_USED:",
//                    stats.memoryUsed / 1024);
//            printRow(pw, TWO_COUNT_COLUMNS_DB, "PAGECACHE_OVERFLOW:",
//                    stats.pageCacheOverflo / 1024, "MALLOC_SIZE:", stats.largestMemAlloc / 1024);
//            pw.println(" ");
//            int N = stats.dbStats.size();
//            if (N > 0) {
//                pw.println(" DATABASES");
//                printRow(pw, "  %8s %8s %14s  %s", "pgsz", "dbsz", "Lookaside(b)", "Dbname");
//                for (int i = 0; i < N; i++) {
//                    DbStats dbStats = stats.dbStats.get(i);
//                    printRow(pw, DB_INFO_FORMAT, dbStats.pageSize, dbStats.dbSize,
//                            dbStats.lookaside, dbStats.dbName);
//                }
//            }
//
//            // Asset details.
//            String assetAlloc = AssetManager.getAssetAllocations();
//            if (assetAlloc != null) {
//                pw.println(" ");
//                pw.println(" Asset Allocations");
//                pw.print(assetAlloc);
//            }
//        }
//
//        private void printRow(PrintWriter pw, String format, Object... objs) {
//            pw.println(String.format(format, objs));
//        }
//    }
//
//    private final class H extends Handler {
//        public static final int LAUNCH_ACTIVITY = 100;
//        public static final int PAUSE_ACTIVITY = 101;
//        public static final int PAUSE_ACTIVITY_FINISHING = 102;
//        public static final int STOP_ACTIVITY_SHOW = 103;
//        public static final int STOP_ACTIVITY_HIDE = 104;
//        public static final int SHOW_WINDOW = 105;
//        public static final int HIDE_WINDOW = 106;
//        public static final int RESUME_ACTIVITY = 107;
//        public static final int SEND_RESULT = 108;
//        public static final int DESTROY_ACTIVITY = 109;
//        public static final int BIND_APPLICATION = 110;
//        public static final int EXIT_APPLICATION = 111;
//        public static final int NEW_INTENT = 112;
//        public static final int RECEIVER = 113;
//        public static final int CREATE_SERVICE = 114;
//        public static final int SERVICE_ARGS = 115;
//        public static final int STOP_SERVICE = 116;
//        public static final int REQUEST_THUMBNAIL = 117;
//        public static final int CONFIGURATION_CHANGED = 118;
//        public static final int CLEAN_UP_CONTEXT = 119;
//        public static final int GC_WHEN_IDLE = 120;
//        public static final int BIND_SERVICE = 121;
//        public static final int UNBIND_SERVICE = 122;
//        public static final int DUMP_SERVICE = 123;
//        public static final int LOW_MEMORY = 124;
//        public static final int ACTIVITY_CONFIGURATION_CHANGED = 125;
//        public static final int RELAUNCH_ACTIVITY = 126;
//        public static final int PROFILER_CONTROL = 127;
//        public static final int CREATE_BACKUP_AGENT = 128;
//        public static final int DESTROY_BACKUP_AGENT = 129;
//        public static final int SUICIDE = 130;
//        public static final int REMOVE_PROVIDER = 131;
//        public static final int ENABLE_JIT = 132;
//        public static final int DISPATCH_PACKAGE_BROADCAST = 133;
//        public static final int SCHEDULE_CRASH = 134;
//
//        String codeToString(int code) {
//            if (DEBUG_MESSAGES) {
//                switch (code) {
//                    case LAUNCH_ACTIVITY:
//                        return "LAUNCH_ACTIVITY";
//                    case PAUSE_ACTIVITY:
//                        return "PAUSE_ACTIVITY";
//                    case PAUSE_ACTIVITY_FINISHING:
//                        return "PAUSE_ACTIVITY_FINISHING";
//                    case STOP_ACTIVITY_SHOW:
//                        return "STOP_ACTIVITY_SHOW";
//                    case STOP_ACTIVITY_HIDE:
//                        return "STOP_ACTIVITY_HIDE";
//                    case SHOW_WINDOW:
//                        return "SHOW_WINDOW";
//                    case HIDE_WINDOW:
//                        return "HIDE_WINDOW";
//                    case RESUME_ACTIVITY:
//                        return "RESUME_ACTIVITY";
//                    case SEND_RESULT:
//                        return "SEND_RESULT";
//                    case DESTROY_ACTIVITY:
//                        return "DESTROY_ACTIVITY";
//                    case BIND_APPLICATION:
//                        return "BIND_APPLICATION";
//                    case EXIT_APPLICATION:
//                        return "EXIT_APPLICATION";
//                    case NEW_INTENT:
//                        return "NEW_INTENT";
//                    case RECEIVER:
//                        return "RECEIVER";
//                    case CREATE_SERVICE:
//                        return "CREATE_SERVICE";
//                    case SERVICE_ARGS:
//                        return "SERVICE_ARGS";
//                    case STOP_SERVICE:
//                        return "STOP_SERVICE";
//                    case REQUEST_THUMBNAIL:
//                        return "REQUEST_THUMBNAIL";
//                    case CONFIGURATION_CHANGED:
//                        return "CONFIGURATION_CHANGED";
//                    case CLEAN_UP_CONTEXT:
//                        return "CLEAN_UP_CONTEXT";
//                    case GC_WHEN_IDLE:
//                        return "GC_WHEN_IDLE";
//                    case BIND_SERVICE:
//                        return "BIND_SERVICE";
//                    case UNBIND_SERVICE:
//                        return "UNBIND_SERVICE";
//                    case DUMP_SERVICE:
//                        return "DUMP_SERVICE";
//                    case LOW_MEMORY:
//                        return "LOW_MEMORY";
//                    case ACTIVITY_CONFIGURATION_CHANGED:
//                        return "ACTIVITY_CONFIGURATION_CHANGED";
//                    case RELAUNCH_ACTIVITY:
//                        return "RELAUNCH_ACTIVITY";
//                    case PROFILER_CONTROL:
//                        return "PROFILER_CONTROL";
//                    case CREATE_BACKUP_AGENT:
//                        return "CREATE_BACKUP_AGENT";
//                    case DESTROY_BACKUP_AGENT:
//                        return "DESTROY_BACKUP_AGENT";
//                    case SUICIDE:
//                        return "SUICIDE";
//                    case REMOVE_PROVIDER:
//                        return "REMOVE_PROVIDER";
//                    case ENABLE_JIT:
//                        return "ENABLE_JIT";
//                    case DISPATCH_PACKAGE_BROADCAST:
//                        return "DISPATCH_PACKAGE_BROADCAST";
//                    case SCHEDULE_CRASH:
//                        return "SCHEDULE_CRASH";
//                }
//            }
//            return "(unknown)";
//        }
//
//        public void handleMessage(Message msg) {
//            if (DEBUG_MESSAGES) Slog.v(TAG, ">>> handling: " + msg.what);
//            switch (msg.what) {
//                case LAUNCH_ACTIVITY: {
//                    ActivityClientRecord r = (ActivityClientRecord) msg.obj;
//
//                    r.packageInfo = getPackageInfoNoCheck(
//                            r.activityInfo.applicationInfo);
//                    handleLaunchActivity(r, null);
//                }
//                break;
//                case RELAUNCH_ACTIVITY: {
//                    ActivityClientRecord r = (ActivityClientRecord) msg.obj;
//                    handleRelaunchActivity(r, msg.arg1);
//                }
//                break;
//                case PAUSE_ACTIVITY:
//                    handlePauseActivity((IBinder) msg.obj, false, msg.arg1 != 0, msg.arg2);
//                    maybeSnapshot();
//                    break;
//                case PAUSE_ACTIVITY_FINISHING:
//                    handlePauseActivity((IBinder) msg.obj, true, msg.arg1 != 0, msg.arg2);
//                    break;
//                case STOP_ACTIVITY_SHOW:
//                    handleStopActivity((IBinder) msg.obj, true, msg.arg2);
//                    break;
//                case STOP_ACTIVITY_HIDE:
//                    handleStopActivity((IBinder) msg.obj, false, msg.arg2);
//                    break;
//                case SHOW_WINDOW:
//                    handleWindowVisibility((IBinder) msg.obj, true);
//                    break;
//                case HIDE_WINDOW:
//                    handleWindowVisibility((IBinder) msg.obj, false);
//                    break;
//                case RESUME_ACTIVITY:
//                    handleResumeActivity((IBinder) msg.obj, true,
//                            msg.arg1 != 0);
//                    break;
//                case SEND_RESULT:
//                    handleSendResult((ResultData) msg.obj);
//                    break;
//                case DESTROY_ACTIVITY:
//                    handleDestroyActivity((IBinder) msg.obj, msg.arg1 != 0,
//                            msg.arg2, false);
//                    break;
//                case BIND_APPLICATION:
//                    AppBindData data = (AppBindData) msg.obj;
//                    handleBindApplication(data);
//                    break;
//                case EXIT_APPLICATION:
//                    if (mInitialApplication != null) {
//                        mInitialApplication.onTerminate();
//                    }
//                    Looper.myLooper().quit();
//                    break;
//                case NEW_INTENT:
//                    handleNewIntent((NewIntentData) msg.obj);
//                    break;
//                case RECEIVER:
//                    handleReceiver((ReceiverData) msg.obj);
//                    maybeSnapshot();
//                    break;
//                case CREATE_SERVICE:
//                    handleCreateService((CreateServiceData) msg.obj);
//                    break;
//                case BIND_SERVICE:
//                    handleBindService((BindServiceData) msg.obj);
//                    break;
//                case UNBIND_SERVICE:
//                    handleUnbindService((BindServiceData) msg.obj);
//                    break;
//                case SERVICE_ARGS:
//                    handleServiceArgs((ServiceArgsData) msg.obj);
//                    break;
//                case STOP_SERVICE:
//                    handleStopService((IBinder) msg.obj);
//                    maybeSnapshot();
//                    break;
//                case REQUEST_THUMBNAIL:
//                    handleRequestThumbnail((IBinder) msg.obj);
//                    break;
//                case CONFIGURATION_CHANGED:
//                    handleConfigurationChanged((Configuration) msg.obj);
//                    break;
//                case CLEAN_UP_CONTEXT:
//                    ContextCleanupInfo cci = (ContextCleanupInfo) msg.obj;
//                    cci.context.performFinalCleanup(cci.who, cci.what);
//                    break;
//                case GC_WHEN_IDLE:
//                    scheduleGcIdler();
//                    break;
//                case DUMP_SERVICE:
//                    handleDumpService((DumpServiceInfo) msg.obj);
//                    break;
//                case LOW_MEMORY:
//                    handleLowMemory();
//                    break;
//                case ACTIVITY_CONFIGURATION_CHANGED:
//                    handleActivityConfigurationChanged((IBinder) msg.obj);
//                    break;
//                case PROFILER_CONTROL:
//                    handleProfilerControl(msg.arg1 != 0, (ProfilerControlData) msg.obj);
//                    break;
//                case CREATE_BACKUP_AGENT:
//                    handleCreateBackupAgent((CreateBackupAgentData) msg.obj);
//                    break;
//                case DESTROY_BACKUP_AGENT:
//                    handleDestroyBackupAgent((CreateBackupAgentData) msg.obj);
//                    break;
//                case SUICIDE:
//                    Process.killProcess(Process.myPid());
//                    break;
//                case REMOVE_PROVIDER:
//                    completeRemoveProvider((IContentProvider) msg.obj);
//                    break;
//                case ENABLE_JIT:
//                    ensureJitEnabled();
//                    break;
//                case DISPATCH_PACKAGE_BROADCAST:
//                    handleDispatchPackageBroadcast(msg.arg1, (String[]) msg.obj);
//                    break;
//                case SCHEDULE_CRASH:
//                    throw new RemoteServiceException((String) msg.obj);
//            }
//            if (DEBUG_MESSAGES) Slog.v(TAG, "<<< done: " + msg.what);
//        }
//
//        void maybeSnapshot() {
//            if (mBoundApplication != null) {
//                SamplingProfilerIntegration.writeSnapshot(
//                        mBoundApplication.processName);
//            }
//        }
//    }
//
//    private final class Idler implements MessageQueue.IdleHandler {
//        public final boolean queueIdle() {
//            ActivityClientRecord a = mNewActivities;
//            if (a != null) {
//                mNewActivities = null;
//                IActivityManager am = ActivityManagerNative.getDefault();
//                ActivityClientRecord prev;
//                do {
//                    if (localLOGV) Slog.v(
//                            TAG, "Reporting idle of " + a +
//                                    " finished=" +
//                                    (a.activity != null ? a.activity.mFinished : false));
//                    if (a.activity != null && !a.activity.mFinished) {
//                        try {
//                            am.activityIdle(a.token, a.createdConfig);
//                            a.createdConfig = null;
//                        } catch (RemoteException ex) {
//                        }
//                    }
//                    prev = a;
//                    a = a.nextIdle;
//                    prev.nextIdle = null;
//                } while (a != null);
//            }
//            ensureJitEnabled();
//            return false;
//        }
//    }
//
//    final class GcIdler implements MessageQueue.IdleHandler {
//        public final boolean queueIdle() {
//            doGcIfNeeded();
//            return false;
//        }
//    }
//
//    private final static class ResourcesKey {
//        final private String mResDir;
//        final private float mScale;
//        final private int mHash;
//
//        ResourcesKey(String resDir, float scale) {
//            mResDir = resDir;
//            mScale = scale;
//            mHash = mResDir.hashCode() << 2 + (int) (mScale * 2);
//        }
//
//        @Override
//        public int hashCode() {
//            return mHash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (!(obj instanceof ResourcesKey)) {
//                return false;
//            }
//            ResourcesKey peer = (ResourcesKey) obj;
//            return mResDir.equals(peer.mResDir) && mScale == peer.mScale;
//        }
//    }
//
//    public static final ActivityThread currentActivityThread() {
//        return sThreadLocal.get();
//    }
//
//    public static final String currentPackageName() {
//        ActivityThread am = currentActivityThread();
//        return (am != null && am.mBoundApplication != null)
//                ? am.mBoundApplication.processName : null;
//    }
//
//    public static final Application currentApplication() {
//        ActivityThread am = currentActivityThread();
//        return am != null ? am.mInitialApplication : null;
//    }
//
//    public static IPackageManager getPackageManager() {
//        if (sPackageManager != null) {
//            //Slog.v("PackageManager", "returning cur default = " + sPackageManager);
//            return sPackageManager;
//        }
//        IBinder b = ServiceManager.getService("package");
//        //Slog.v("PackageManager", "default service binder = " + b);
//        sPackageManager = IPackageManager.Stub.asInterface(b);
//        //Slog.v("PackageManager", "default service = " + sPackageManager);
//        return sPackageManager;
//    }
//
//    DisplayMetrics getDisplayMetricsLocked(boolean forceUpdate) {
//        if (mDisplayMetrics != null && !forceUpdate) {
//            return mDisplayMetrics;
//        }
//        if (mDisplay == null) {
//            WindowManager wm = WindowManagerImpl.getDefault();
//            mDisplay = wm.getDefaultDisplay();
//        }
//        DisplayMetrics metrics = mDisplayMetrics = new DisplayMetrics();
//        mDisplay.getMetrics(metrics);
//        //Slog.i("foo", "New metrics: w=" + metrics.widthPixels + " h="
//        //        + metrics.heightPixels + " den=" + metrics.density
//        //        + " xdpi=" + metrics.xdpi + " ydpi=" + metrics.ydpi);
//        return metrics;
//    }
//
//    /**
//     * Creates the top level Resources for applications with the given compatibility info.
//     *
//     * @param resDir   the resource directory.
//     * @param compInfo the compability info. It will use the default compatibility info when it's
//     *                 null.
//     */
//    Resources getTopLevelResources(String resDir, CompatibilityInfo compInfo) {
//        ResourcesKey key = new ResourcesKey(resDir, compInfo.applicationScale);
//        Resources r;
//        synchronized (mPackages) {
//            // Resources is app scale dependent.
//            if (false) {
//                Slog.w(TAG, "getTopLevelResources: " + resDir + " / "
//                        + compInfo.applicationScale);
//            }
//            WeakReference<resources> wr = mActiveResources.get(key);
//            r = wr != null ? wr.get() : null;
//            //if (r != null) Slog.i(TAG, "isUpToDate " + resDir + ": " + r.getAssets().isUpToDate());
//            if (r != null && r.getAssets().isUpToDate()) {
//                if (false) {
//                    Slog.w(TAG, "Returning cached resources " + r + " " + resDir
//                            + ": appScale=" + r.getCompatibilityInfo().applicationScale);
//                }
//                return r;
//            }
//        }
//
//        //if (r != null) {
//        //    Slog.w(TAG, "Throwing away out-of-date resources!!!! "
//        //            + r + " " + resDir);
//        //}
//
//        AssetManager assets = new AssetManager();
//        if (assets.addAssetPath(resDir) == 0) {
//            return null;
//        }
//
//        //Slog.i(TAG, "Resource: key=" + key + ", display metrics=" + metrics);
//        DisplayMetrics metrics = getDisplayMetricsLocked(false);
//        r = new Resources(assets, metrics, getConfiguration(), compInfo);
//        if (false) {
//            Slog.i(TAG, "Created app resources " + resDir + " " + r + ": "
//                    + r.getConfiguration() + " appScale="
//                    + r.getCompatibilityInfo().applicationScale);
//        }
//
//        synchronized (mPackages) {
//            WeakReference<resources> wr = mActiveResources.get(key);
//            Resources existing = wr != null ? wr.get() : null;
//            if (existing != null && existing.getAssets().isUpToDate()) {
//                // Someone else already created the resources while we were
//                // unlocked; go ahead and use theirs.
//                r.getAssets().close();
//                return existing;
//            }
//
//            // XXX need to remove entries when weak references go away
//            mActiveResources.put(key, new WeakReference<resources>(r));
//            return r;
//        }
//    }
//
//    /**
//     * Creates the top level resources for the given package.
//     */
//    Resources getTopLevelResources(String resDir, LoadedApk pkgInfo) {
//        return getTopLevelResources(resDir, pkgInfo.mCompatibilityInfo);
//    }
//
//    final Handler getHandler() {
//        return mH;
//    }
//
//    public final LoadedApk getPackageInfo(String packageName, int flags) {
//        synchronized (mPackages) {
//            WeakReference<loadedapk> ref;
//            if ((flags & Context.CONTEXT_INCLUDE_CODE) != 0) {
//                ref = mPackages.get(packageName);
//            } else {
//                ref = mResourcePackages.get(packageName);
//            }
//            LoadedApk packageInfo = ref != null ? ref.get() : null;
//            //Slog.i(TAG, "getPackageInfo " + packageName + ": " + packageInfo);
//            //if (packageInfo != null) Slog.i(TAG, "isUptoDate " + packageInfo.mResDir
//            //        + ": " + packageInfo.mResources.getAssets().isUpToDate());
//            if (packageInfo != null && (packageInfo.mResources == null
//                    || packageInfo.mResources.getAssets().isUpToDate())) {
//                if (packageInfo.isSecurityViolation()
//                        && (flags & Context.CONTEXT_IGNORE_SECURITY) == 0) {
//                    throw new SecurityException(
//                            "Requesting code from " + packageName
//                                    + " to be run in process "
//                                    + mBoundApplication.processName
//                                    + "/" + mBoundApplication.appInfo.uid);
//                }
//                return packageInfo;
//            }
//        }
//
//        ApplicationInfo ai = null;
//        try {
//            ai = getPackageManager().getApplicationInfo(packageName,
//                    PackageManager.GET_SHARED_LIBRARY_FILES);
//        } catch (RemoteException e) {
//        }
//
//        if (ai != null) {
//            return getPackageInfo(ai, flags);
//        }
//
//        return null;
//    }
//
//    public final LoadedApk getPackageInfo(ApplicationInfo ai, int flags) {
//        boolean includeCode = (flags & Context.CONTEXT_INCLUDE_CODE) != 0;
//        boolean securityViolation = includeCode && ai.uid != 0
//                && ai.uid != Process.SYSTEM_UID && (mBoundApplication != null
//                ? ai.uid != mBoundApplication.appInfo.uid : true);
//        if ((flags & (Context.CONTEXT_INCLUDE_CODE
//                | Context.CONTEXT_IGNORE_SECURITY))
//                == Context.CONTEXT_INCLUDE_CODE) {
//            if (securityViolation) {
//                String msg = "Requesting code from " + ai.packageName
//                        + " (with uid " + ai.uid + ")";
//                if (mBoundApplication != null) {
//                    msg = msg + " to be run in process "
//                            + mBoundApplication.processName + " (with uid "
//                            + mBoundApplication.appInfo.uid + ")";
//                }
//                throw new SecurityException(msg);
//            }
//        }
//        return getPackageInfo(ai, null, securityViolation, includeCode);
//    }
//
//    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai) {
//        return getPackageInfo(ai, null, false, true);
//    }
//
//    private final LoadedApk getPackageInfo(ApplicationInfo aInfo,
//                                           ClassLoader baseLoader, boolean securityViolation, boolean includeCode) {
//        synchronized (mPackages) {
//            WeakReference<loadedapk> ref;
//            if (includeCode) {
//                ref = mPackages.get(aInfo.packageName);
//            } else {
//                ref = mResourcePackages.get(aInfo.packageName);
//            }
//            LoadedApk packageInfo = ref != null ? ref.get() : null;
//            if (packageInfo == null || (packageInfo.mResources != null
//                    && !packageInfo.mResources.getAssets().isUpToDate())) {
//                if (localLOGV) Slog.v(TAG, (includeCode ? "Loading code package "
//                        : "Loading resource-only package ") + aInfo.packageName
//                        + " (in " + (mBoundApplication != null
//                        ? mBoundApplication.processName : null)
//                        + ")");
//                packageInfo =
//                        new LoadedApk(this, aInfo, this, baseLoader,
//                                securityViolation, includeCode &&
//                                (aInfo.flags & ApplicationInfo.FLAG_HAS_CODE) != 0);
//                if (includeCode) {
//                    mPackages.put(aInfo.packageName,
//                            new WeakReference<loadedapk>(packageInfo));
//                } else {
//                    mResourcePackages.put(aInfo.packageName,
//                            new WeakReference<loadedapk>(packageInfo));
//                }
//            }
//            return packageInfo;
//        }
//    }
//
//    ActivityThread() {
//    }
//
//    public ApplicationThread getApplicationThread() {
//        return mAppThread;
//    }
//
//    public Instrumentation getInstrumentation() {
//        return mInstrumentation;
//    }
//
//    public Configuration getConfiguration() {
//        return mConfiguration;
//    }
//
//    public boolean isProfiling() {
//        return mBoundApplication != null && mBoundApplication.profileFile != null;
//    }
//
//    public String getProfileFilePath() {
//        return mBoundApplication.profileFile;
//    }
//
//    public Looper getLooper() {
//        return mLooper;
//    }
//
//    public Application getApplication() {
//        return mInitialApplication;
//    }
//
//    public String getProcessName() {
//        return mBoundApplication.processName;
//    }
//
//    public ContextImpl getSystemContext() {
//        synchronized (this) {
//            if (mSystemContext == null) {
//                ContextImpl context =
//                        ContextImpl.createSystemContext(this);
//                LoadedApk info = new LoadedApk(this, "android", context, null);
//                context.init(info, null, this);
//                context.getResources().updateConfiguration(
//                        getConfiguration(), getDisplayMetricsLocked(false));
//                mSystemContext = context;
//                //Slog.i(TAG, "Created system resources " + context.getResources()
//                //        + ": " + context.getResources().getConfiguration());
//            }
//        }
//        return mSystemContext;
//    }
//
//    public void installSystemApplicationInfo(ApplicationInfo info) {
//        synchronized (this) {
//            ContextImpl context = getSystemContext();
//            context.init(new LoadedApk(this, "android", context, info), null, this);
//        }
//    }
//
//    void ensureJitEnabled() {
//        if (!mJitEnabled) {
//            mJitEnabled = true;
//            dalvik.system.VMRuntime.getRuntime().startJitCompilation();
//        }
//    }
//
//    void scheduleGcIdler() {
//        if (!mGcIdlerScheduled) {
//            mGcIdlerScheduled = true;
//            Looper.myQueue().addIdleHandler(mGcIdler);
//        }
//        mH.removeMessages(H.GC_WHEN_IDLE);
//    }
//
//    void unscheduleGcIdler() {
//        if (mGcIdlerScheduled) {
//            mGcIdlerScheduled = false;
//            Looper.myQueue().removeIdleHandler(mGcIdler);
//        }
//        mH.removeMessages(H.GC_WHEN_IDLE);
//    }
//
//    void doGcIfNeeded() {
//        mGcIdlerScheduled = false;
//        final long now = SystemClock.uptimeMillis();
//        //Slog.i(TAG, "**** WE MIGHT WANT TO GC: then=" + Binder.getLastGcTime()
//        //        + "m now=" + now);
//        if ((BinderInternal.getLastGcTime() + MIN_TIME_BETWEEN_GCS) < now) {
//            //Slog.i(TAG, "**** WE DO, WE DO WANT TO GC!");
//            BinderInternal.forceGc("bg");
//        }
//    }
//
//    public final ActivityInfo resolveActivityInfo(Intent intent) {
//        ActivityInfo aInfo = intent.resolveActivityInfo(
//                mInitialApplication.getPackageManager(), PackageManager.GET_SHARED_LIBRARY_FILES);
//        if (aInfo == null) {
//            // Throw an exception.
//            Instrumentation.checkStartActivityResult(
//                    IActivityManager.START_CLASS_NOT_FOUND, intent);
//        }
//        return aInfo;
//    }
//
//    public final Activity startActivityNow(Activity parent, String id,
//                                           Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state,
//                                           Object lastNonConfigurationInstance) {
//        ActivityClientRecord r = new ActivityClientRecord();
//        r.token = token;
//        r.ident = 0;
//        r.intent = intent;
//        r.state = state;
//        r.parent = parent;
//        r.embeddedID = id;
//        r.activityInfo = activityInfo;
//        r.lastNonConfigurationInstance = lastNonConfigurationInstance;
//        if (localLOGV) {
//            ComponentName compname = intent.getComponent();
//            String name;
//            if (compname != null) {
//                name = compname.toShortString();
//            } else {
//                name = "(Intent " + intent + ").getComponent() returned null";
//            }
//            Slog.v(TAG, "Performing launch: action=" + intent.getAction()
//                    + ", comp=" + name
//                    + ", token=" + token);
//        }
//        return performLaunchActivity(r, null);
//    }
//
//    public final Activity getActivity(IBinder token) {
//        return mActivities.get(token).activity;
//    }
//
//    public final void sendActivityResult(
//            IBinder token, String id, int requestCode,
//            int resultCode, Intent data) {
//        if (DEBUG_RESULTS) Slog.v(TAG, "sendActivityResult: id=" + id
//                + " req=" + requestCode + " res=" + resultCode + " data=" + data);
//        ArrayList<resultinfo> list = new ArrayList<resultinfo>();
//        list.add(new ResultInfo(id, requestCode, resultCode, data));
//        mAppThread.scheduleSendResult(token, list);
//    }
//
//    // if the thread hasn't started yet, we don't have the handler, so just
//    // save the messages until we're ready.
//    private final void queueOrSendMessage(int what, Object obj) {
//        queueOrSendMessage(what, obj, 0, 0);
//    }
//
//    private final void queueOrSendMessage(int what, Object obj, int arg1) {
//        queueOrSendMessage(what, obj, arg1, 0);
//    }
//
//    private final void queueOrSendMessage(int what, Object obj, int arg1, int arg2) {
//        synchronized (this) {
//            if (DEBUG_MESSAGES) Slog.v(
//                    TAG, "SCHEDULE " + what + " " + mH.codeToString(what)
//                            + ": " + arg1 + " / " + obj);
//            Message msg = Message.obtain();
//            msg.what = what;
//            msg.obj = obj;
//            msg.arg1 = arg1;
//            msg.arg2 = arg2;
//            mH.sendMessage(msg);
//        }
//    }
//
//    final void scheduleContextCleanup(ContextImpl context, String who,
//                                      String what) {
//        ContextCleanupInfo cci = new ContextCleanupInfo();
//        cci.context = context;
//        cci.who = who;
//        cci.what = what;
//        queueOrSendMessage(H.CLEAN_UP_CONTEXT, cci);
//    }
//
//    private final Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
//        // System.out.println("##### [" + System.currentTimeMillis() + "] ActivityThread.performLaunchActivity(" + r + ")");
//
//        ActivityInfo aInfo = r.activityInfo;
//        if (r.packageInfo == null) {
//            r.packageInfo = getPackageInfo(aInfo.applicationInfo,
//                    Context.CONTEXT_INCLUDE_CODE);
//        }
//
//        ComponentName component = r.intent.getComponent();
//        if (component == null) {
//            component = r.intent.resolveActivity(
//                    mInitialApplication.getPackageManager());
//            r.intent.setComponent(component);
//        }
//
//        if (r.activityInfo.targetActivity != null) {
//            component = new ComponentName(r.activityInfo.packageName,
//                    r.activityInfo.targetActivity);
//        }
//
//        Activity activity = null;
//        try {
//            java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
//            activity = mInstrumentation.newActivity(
//                    cl, component.getClassName(), r.intent);
//            r.intent.setExtrasClassLoader(cl);
//            if (r.state != null) {
//                r.state.setClassLoader(cl);
//            }
//        } catch (Exception e) {
//            if (!mInstrumentation.onException(activity, e)) {
//                throw new RuntimeException(
//                        "Unable to instantiate activity " + component
//                                + ": " + e.toString(), e);
//            }
//        }
//
//        try {
//            Application app = r.packageInfo.makeApplication(false, mInstrumentation);
//
//            if (localLOGV) Slog.v(TAG, "Performing launch of " + r);
//            if (localLOGV) Slog.v(
//                    TAG, r + ": app=" + app
//                            + ", appName=" + app.getPackageName()
//                            + ", pkg=" + r.packageInfo.getPackageName()
//                            + ", comp=" + r.intent.getComponent().toShortString()
//                            + ", dir=" + r.packageInfo.getAppDir());
//
//            if (activity != null) {
//                ContextImpl appContext = new ContextImpl();
//                appContext.init(r.packageInfo, r.token, this);
//                appContext.setOuterContext(activity);
//                CharSequence title = r.activityInfo.loadLabel(appContext.getPackageManager());
//                Configuration config = new Configuration(mConfiguration);
//                if (DEBUG_CONFIGURATION) Slog.v(TAG, "Launching activity "
//                        + r.activityInfo.name + " with config " + config);
//                activity.attach(appContext, this, getInstrumentation(), r.token,
//                        r.ident, app, r.intent, r.activityInfo, title, r.parent,
//                        r.embeddedID, r.lastNonConfigurationInstance,
//                        r.lastNonConfigurationChildInstances, config);
//
//                if (customIntent != null) {
//                    activity.mIntent = customIntent;
//                }
//                r.lastNonConfigurationInstance = null;
//                r.lastNonConfigurationChildInstances = null;
//                activity.mStartedActivity = false;
//                int theme = r.activityInfo.getThemeResource();
//                if (theme != 0) {
//                    activity.setTheme(theme);
//                }
//
//                activity.mCalled = false;
//                mInstrumentation.callActivityOnCreate(activity, r.state);
//                if (!activity.mCalled) {
//                    throw new SuperNotCalledException(
//                            "Activity " + r.intent.getComponent().toShortString() +
//                                    " did not call through to super.onCreate()");
//                }
//                r.activity = activity;
//                r.stopped = true;
//                if (!r.activity.mFinished) {
//                    activity.performStart();
//                    r.stopped = false;
//                }
//                if (!r.activity.mFinished) {
//                    if (r.state != null) {
//                        mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
//                    }
//                }
//                if (!r.activity.mFinished) {
//                    activity.mCalled = false;
//                    mInstrumentation.callActivityOnPostCreate(activity, r.state);
//                    if (!activity.mCalled) {
//                        throw new SuperNotCalledException(
//                                "Activity " + r.intent.getComponent().toShortString() +
//                                        " did not call through to super.onPostCreate()");
//                    }
//                }
//            }
//            r.paused = true;
//
//            mActivities.put(r.token, r);
//
//        } catch (SuperNotCalledException e) {
//            throw e;
//
//        } catch (Exception e) {
//            if (!mInstrumentation.onException(activity, e)) {
//                throw new RuntimeException(
//                        "Unable to start activity " + component
//                                + ": " + e.toString(), e);
//            }
//        }
//
//        return activity;
//    }
//
//    private final void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
//        // If we are getting ready to gc after going to the background, well
//        // we are back active so skip it.
//        unscheduleGcIdler();
//
//        if (localLOGV) Slog.v(
//                TAG, "Handling launch of " + r);
//        Activity a = performLaunchActivity(r, customIntent);
//
//        if (a != null) {
//            r.createdConfig = new Configuration(mConfiguration);
//            Bundle oldState = r.state;
//            handleResumeActivity(r.token, false, r.isForward);
//
//            if (!r.activity.mFinished && r.startsNotResumed) {
//                // The activity manager actually wants this one to start out
//                // paused, because it needs to be visible but isn't in the
//                // foreground.  We accomplish this by going through the
//                // normal startup (because activities expect to go through
//                // onResume() the first time they run, before their window
//                // is displayed), and then pausing it.  However, in this case
//                // we do -not- need to do the full pause cycle (of freezing
//                // and such) because the activity manager assumes it can just
//                // retain the current state it has.
//                try {
//                    r.activity.mCalled = false;
//                    mInstrumentation.callActivityOnPause(r.activity);
//                    // We need to keep around the original state, in case
//                    // we need to be created again.
//                    r.state = oldState;
//                    if (!r.activity.mCalled) {
//                        throw new SuperNotCalledException(
//                                "Activity " + r.intent.getComponent().toShortString() +
//                                        " did not call through to super.onPause()");
//                    }
//
//                } catch (SuperNotCalledException e) {
//                    throw e;
//
//                } catch (Exception e) {
//                    if (!mInstrumentation.onException(r.activity, e)) {
//                        throw new RuntimeException(
//                                "Unable to pause activity "
//                                        + r.intent.getComponent().toShortString()
//                                        + ": " + e.toString(), e);
//                    }
//                }
//                r.paused = true;
//            }
//        } else {
//            // If there was an error, for any reason, tell the activity
//            // manager to stop us.
//            try {
//                ActivityManagerNative.getDefault()
//                        .finishActivity(r.token, Activity.RESULT_CANCELED, null);
//            } catch (RemoteException ex) {
//            }
//        }
//    }
//
//    private final void deliverNewIntents(ActivityClientRecord r,
//                                         List<intent> intents) {
//        final int N = intents.size();
//        for (int i = 0; i < n; final="" ibinder = "" intent = "" public="" void="" > intents){
//            ActivityClientRecord r = mActivities.get(token);
//            if (r != null) {
//                final boolean resumed = !r.paused;
//                if (resumed) {
//                    mInstrumentation.callActivityOnPause(r.activity);
//                }
//                deliverNewIntents(r, intents);
//                if (resumed) {
//                    mInstrumentation.callActivityOnResume(r.activity);
//                }
//            }
//        }
//
//        private final void handleNewIntent (NewIntentData data){
//            performNewIntents(data.token, data.intents);
//        }
//
//        private final void handleReceiver (ReceiverData data){
//            // If we are getting ready to gc after going to the background, well
//            // we are back active so skip it.
//            unscheduleGcIdler();
//
//            String component = data.intent.getComponent().getClassName();
//
//            LoadedApk packageInfo = getPackageInfoNoCheck(
//                    data.info.applicationInfo);
//
//            IActivityManager mgr = ActivityManagerNative.getDefault();
//
//            BroadcastReceiver receiver = null;
//            try {
//                java.lang.ClassLoader cl = packageInfo.getClassLoader();
//                data.intent.setExtrasClassLoader(cl);
//                if (data.resultExtras != null) {
//                    data.resultExtras.setClassLoader(cl);
//                }
//                receiver = (BroadcastReceiver) cl.loadClass(component).newInstance();
//            } catch (Exception e) {
//                try {
//                    if (DEBUG_BROADCAST) Slog.i(TAG,
//                            "Finishing failed broadcast to " + data.intent.getComponent());
//                    mgr.finishReceiver(mAppThread.asBinder(), data.resultCode,
//                            data.resultData, data.resultExtras, data.resultAbort);
//                } catch (RemoteException ex) {
//                }
//                throw new RuntimeException(
//                        "Unable to instantiate receiver " + component
//                                + ": " + e.toString(), e);
//            }
//
//            try {
//                Application app = packageInfo.makeApplication(false, mInstrumentation);
//
//                if (localLOGV) Slog.v(
//                        TAG, "Performing receive of " + data.intent
//                                + ": app=" + app
//                                + ", appName=" + app.getPackageName()
//                                + ", pkg=" + packageInfo.getPackageName()
//                                + ", comp=" + data.intent.getComponent().toShortString()
//                                + ", dir=" + packageInfo.getAppDir());
//
//                ContextImpl context = (ContextImpl) app.getBaseContext();
//                receiver.setOrderedHint(true);
//                receiver.setResult(data.resultCode, data.resultData,
//                        data.resultExtras);
//                receiver.setOrderedHint(data.sync);
//                receiver.onReceive(context.getReceiverRestrictedContext(),
//                        data.intent);
//            } catch (Exception e) {
//                try {
//                    if (DEBUG_BROADCAST) Slog.i(TAG,
//                            "Finishing failed broadcast to " + data.intent.getComponent());
//                    mgr.finishReceiver(mAppThread.asBinder(), data.resultCode,
//                            data.resultData, data.resultExtras, data.resultAbort);
//                } catch (RemoteException ex) {
//                }
//                if (!mInstrumentation.onException(receiver, e)) {
//                    throw new RuntimeException(
//                            "Unable to start receiver " + component
//                                    + ": " + e.toString(), e);
//                }
//            }
//
//            QueuedWork.waitToFinish();
//
//            try {
//                if (data.sync) {
//                    if (DEBUG_BROADCAST) Slog.i(TAG,
//                            "Finishing ordered broadcast to " + data.intent.getComponent());
//                    mgr.finishReceiver(
//                            mAppThread.asBinder(), receiver.getResultCode(),
//                            receiver.getResultData(), receiver.getResultExtras(false),
//                            receiver.getAbortBroadcast());
//                } else {
//                    if (DEBUG_BROADCAST) Slog.i(TAG,
//                            "Finishing broadcast to " + data.intent.getComponent());
//                    mgr.finishReceiver(mAppThread.asBinder(), 0, null, null, false);
//                }
//            } catch (RemoteException ex) {
//            }
//        }
//
//        // Instantiate a BackupAgent and tell it that it's alive
//        private final void handleCreateBackupAgent (CreateBackupAgentData data){
//            if (DEBUG_BACKUP) Slog.v(TAG, "handleCreateBackupAgent: " + data);
//
//            // no longer idle; we have backup work to do
//            unscheduleGcIdler();
//
//            // instantiate the BackupAgent class named in the manifest
//            LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo);
//            String packageName = packageInfo.mPackageName;
//            if (mBackupAgents.get(packageName) != null) {
//                Slog.d(TAG, "BackupAgent " + "  for " + packageName
//                        + " already exists");
//                return;
//            }
//
//            BackupAgent agent = null;
//            String classname = data.appInfo.backupAgentName;
//            if (classname == null) {
//                if (data.backupMode == IApplicationThread.BACKUP_MODE_INCREMENTAL) {
//                    Slog.e(TAG, "Attempted incremental backup but no defined agent for "
//                            + packageName);
//                    return;
//                }
//                classname = "android.app.FullBackupAgent";
//            }
//            try {
//                IBinder binder = null;
//                try {
//                    java.lang.ClassLoader cl = packageInfo.getClassLoader();
//                    agent = (BackupAgent) cl.loadClass(data.appInfo.backupAgentName).newInstance();
//
//                    // set up the agent's context
//                    if (DEBUG_BACKUP) Slog.v(TAG, "Initializing BackupAgent "
//                            + data.appInfo.backupAgentName);
//
//                    ContextImpl context = new ContextImpl();
//                    context.init(packageInfo, null, this);
//                    context.setOuterContext(agent);
//                    agent.attach(context);
//
//                    agent.onCreate();
//                    binder = agent.onBind();
//                    mBackupAgents.put(packageName, agent);
//                } catch (Exception e) {
//                    // If this is during restore, fail silently; otherwise go
//                    // ahead and let the user see the crash.
//                    Slog.e(TAG, "Agent threw during creation: " + e);
//                    if (data.backupMode != IApplicationThread.BACKUP_MODE_RESTORE) {
//                        throw e;
//                    }
//                    // falling through with 'binder' still null
//                }
//
//                // tell the OS that we're live now
//                try {
//                    ActivityManagerNative.getDefault().backupAgentCreated(packageName, binder);
//                } catch (RemoteException e) {
//                    // nothing to do.
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Unable to create BackupAgent "
//                        + data.appInfo.backupAgentName + ": " + e.toString(), e);
//            }
//        }
//
//        // Tear down a BackupAgent
//        private final void handleDestroyBackupAgent (CreateBackupAgentData data){
//            if (DEBUG_BACKUP) Slog.v(TAG, "handleDestroyBackupAgent: " + data);
//
//            LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo);
//            String packageName = packageInfo.mPackageName;
//            BackupAgent agent = mBackupAgents.get(packageName);
//            if (agent != null) {
//                try {
//                    agent.onDestroy();
//                } catch (Exception e) {
//                    Slog.w(TAG, "Exception thrown in onDestroy by backup agent of " + data.appInfo);
//                    e.printStackTrace();
//                }
//                mBackupAgents.remove(packageName);
//            } else {
//                Slog.w(TAG, "Attempt to destroy unknown backup agent " + data);
//            }
//        }
//
//        private final void handleCreateService (CreateServiceData data){
//            // If we are getting ready to gc after going to the background, well
//            // we are back active so skip it.
//            unscheduleGcIdler();
//
//            LoadedApk packageInfo = getPackageInfoNoCheck(
//                    data.info.applicationInfo);
//            Service service = null;
//            try {
//                java.lang.ClassLoader cl = packageInfo.getClassLoader();
//                service = (Service) cl.loadClass(data.info.name).newInstance();
//            } catch (Exception e) {
//                if (!mInstrumentation.onException(service, e)) {
//                    throw new RuntimeException(
//                            "Unable to instantiate service " + data.info.name
//                                    + ": " + e.toString(), e);
//                }
//            }
//
//            try {
//                if (localLOGV) Slog.v(TAG, "Creating service " + data.info.name);
//
//                ContextImpl context = new ContextImpl();
//                context.init(packageInfo, null, this);
//
//                Application app = packageInfo.makeApplication(false, mInstrumentation);
//                context.setOuterContext(service);
//                service.attach(context, this, data.info.name, data.token, app,
//                        ActivityManagerNative.getDefault());
//                service.onCreate();
//                mServices.put(data.token, service);
//                try {
//                    ActivityManagerNative.getDefault().serviceDoneExecuting(
//                            data.token, 0, 0, 0);
//                } catch (RemoteException e) {
//                    // nothing to do.
//                }
//            } catch (Exception e) {
//                if (!mInstrumentation.onException(service, e)) {
//                    throw new RuntimeException(
//                            "Unable to create service " + data.info.name
//                                    + ": " + e.toString(), e);
//                }
//            }
//        }
//
//        private final void handleBindService (BindServiceData data){
//            Service s = mServices.get(data.token);
//            if (s != null) {
//                try {
//                    data.intent.setExtrasClassLoader(s.getClassLoader());
//                    try {
//                        if (!data.rebind) {
//                            IBinder binder = s.onBind(data.intent);
//                            ActivityManagerNative.getDefault().publishService(
//                                    data.token, data.intent, binder);
//                        } else {
//                            s.onRebind(data.intent);
//                            ActivityManagerNative.getDefault().serviceDoneExecuting(
//                                    data.token, 0, 0, 0);
//                        }
//                        ensureJitEnabled();
//                    } catch (RemoteException ex) {
//                    }
//                } catch (Exception e) {
//                    if (!mInstrumentation.onException(s, e)) {
//                        throw new RuntimeException(
//                                "Unable to bind to service " + s
//                                        + " with " + data.intent + ": " + e.toString(), e);
//                    }
//                }
//            }
//        }
//
//        private final void handleUnbindService (BindServiceData data){
//            Service s = mServices.get(data.token);
//            if (s != null) {
//                try {
//                    data.intent.setExtrasClassLoader(s.getClassLoader());
//                    boolean doRebind = s.onUnbind(data.intent);
//                    try {
//                        if (doRebind) {
//                            ActivityManagerNative.getDefault().unbindFinished(
//                                    data.token, data.intent, doRebind);
//                        } else {
//                            ActivityManagerNative.getDefault().serviceDoneExecuting(
//                                    data.token, 0, 0, 0);
//                        }
//                    } catch (RemoteException ex) {
//                    }
//                } catch (Exception e) {
//                    if (!mInstrumentation.onException(s, e)) {
//                        throw new RuntimeException(
//                                "Unable to unbind to service " + s
//                                        + " with " + data.intent + ": " + e.toString(), e);
//                    }
//                }
//            }
//        }
//
//        private void handleDumpService (DumpServiceInfo info){
//            try {
//                Service s = mServices.get(info.service);
//                if (s != null) {
//                    PrintWriter pw = new PrintWriter(new FileOutputStream(info.fd));
//                    s.dump(info.fd, pw, info.args);
//                    pw.close();
//                }
//            } finally {
//                synchronized (info) {
//                    info.dumped = true;
//                    info.notifyAll();
//                }
//            }
//        }
//
//        private final void handleServiceArgs (ServiceArgsData data){
//            Service s = mServices.get(data.token);
//            if (s != null) {
//                try {
//                    if (data.args != null) {
//                        data.args.setExtrasClassLoader(s.getClassLoader());
//                    }
//                    int res = s.onStartCommand(data.args, data.flags, data.startId);
//
//                    QueuedWork.waitToFinish();
//
//                    try {
//                        ActivityManagerNative.getDefault().serviceDoneExecuting(
//                                data.token, 1, data.startId, res);
//                    } catch (RemoteException e) {
//                        // nothing to do.
//                    }
//                    ensureJitEnabled();
//                } catch (Exception e) {
//                    if (!mInstrumentation.onException(s, e)) {
//                        throw new RuntimeException(
//                                "Unable to start service " + s
//                                        + " with " + data.args + ": " + e.toString(), e);
//                    }
//                }
//            }
//        }
//
//        private final void handleStopService (IBinder token){
//            Service s = mServices.remove(token);
//            if (s != null) {
//                try {
//                    if (localLOGV) Slog.v(TAG, "Destroying service " + s);
//                    s.onDestroy();
//                    Context context = s.getBaseContext();
//                    if (context instanceof ContextImpl) {
//                        final String who = s.getClassName();
//                        ((ContextImpl) context).scheduleFinalCleanup(who, "Service");
//                    }
//
//                    QueuedWork.waitToFinish();
//
//                    try {
//                        ActivityManagerNative.getDefault().serviceDoneExecuting(
//                                token, 0, 0, 0);
//                    } catch (RemoteException e) {
//                        // nothing to do.
//                    }
//                } catch (Exception e) {
//                    if (!mInstrumentation.onException(s, e)) {
//                        throw new RuntimeException(
//                                "Unable to stop service " + s
//                                        + ": " + e.toString(), e);
//                    }
//                }
//            }
//            //Slog.i(TAG, "Running services: " + mServices);
//        }
//
//        public final ActivityClientRecord performResumeActivity (IBinder token,
//        boolean clearHide){
//            ActivityClientRecord r = mActivities.get(token);
//            if (localLOGV) Slog.v(TAG, "Performing resume of " + r
//                    + " finished=" + r.activity.mFinished);
//            if (r != null && !r.activity.mFinished) {
//                if (clearHide) {
//                    r.hideForNow = false;
//                    r.activity.mStartedActivity = false;
//                }
//                try {
//                    if (r.pendingIntents != null) {
//                        deliverNewIntents(r, r.pendingIntents);
//                        r.pendingIntents = null;
//                    }
//                    if (r.pendingResults != null) {
//                        deliverResults(r, r.pendingResults);
//                        r.pendingResults = null;
//                    }
//                    r.activity.performResume();
//
//                    EventLog.writeEvent(LOG_ON_RESUME_CALLED,
//                            r.activity.getComponentName().getClassName());
//
//                    r.paused = false;
//                    r.stopped = false;
//                    r.state = null;
//                } catch (Exception e) {
//                    if (!mInstrumentation.onException(r.activity, e)) {
//                        throw new RuntimeException(
//                                "Unable to resume activity "
//                                        + r.intent.getComponent().toShortString()
//                                        + ": " + e.toString(), e);
//                    }
//                }
//            }
//            return r;
//        }
//
//        final void handleResumeActivity (IBinder token,boolean clearHide, boolean isForward){
//            // If we are getting ready to gc after going to the background, well
//            // we are back active so skip it.
//            unscheduleGcIdler();
//
//            ActivityClientRecord r = performResumeActivity(token, clearHide);
//
//            if (r != null) {
//                final Activity a = r.activity;
//
//                if (localLOGV) Slog.v(
//                        TAG, "Resume " + r + " started activity: " +
//                                a.mStartedActivity + ", hideForNow: " + r.hideForNow
//                                + ", finished: " + a.mFinished);
//
//                final int forwardBit = isForward ?
//                        WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION : 0;
//
//                // If the window hasn't yet been added to the window manager,
//                // and this guy didn't finish itself or start another activity,
//                // then go ahead and add the window.
//                boolean willBeVisible = !a.mStartedActivity;
//                if (!willBeVisible) {
//                    try {
//                        willBeVisible = ActivityManagerNative.getDefault().willActivityBeVisible(
//                                a.getActivityToken());
//                    } catch (RemoteException e) {
//                    }
//                }
//                if (r.window == null && !a.mFinished && willBeVisible) {
//                    r.window = r.activity.getWindow();
//                    View decor = r.window.getDecorView();
//                    decor.setVisibility(View.INVISIBLE);
//                    ViewManager wm = a.getWindowManager();
//                    WindowManager.LayoutParams l = r.window.getAttributes();
//                    a.mDecor = decor;
//                    l.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
//                    l.softInputMode |= forwardBit;
//                    if (a.mVisibleFromClient) {
//                        a.mWindowAdded = true;
//                        wm.addView(decor, l);
//                    }
//
//                    // If the window has already been added, but during resume
//                    // we started another activity, then don't yet make the
//                    // window visible.
//                } else if (!willBeVisible) {
//                    if (localLOGV) Slog.v(
//                            TAG, "Launch " + r + " mStartedActivity set");
//                    r.hideForNow = true;
//                }
//
//                // The window is now visible if it has been added, we are not
//                // simply finishing, and we are not starting another activity.
//                if (!r.activity.mFinished && willBeVisible
//                        && r.activity.mDecor != null && !r.hideForNow) {
//                    if (r.newConfig != null) {
//                        if (DEBUG_CONFIGURATION) Slog.v(TAG, "Resuming activity "
//                                + r.activityInfo.name + " with newConfig " + r.newConfig);
//                        performConfigurationChanged(r.activity, r.newConfig);
//                        r.newConfig = null;
//                    }
//                    if (localLOGV) Slog.v(TAG, "Resuming " + r + " with isForward="
//                            + isForward);
//                    WindowManager.LayoutParams l = r.window.getAttributes();
//                    if ((l.softInputMode
//                            & WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION)
//                            != forwardBit) {
//                        l.softInputMode = (l.softInputMode
//                                & (~WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION))
//                                | forwardBit;
//                        if (r.activity.mVisibleFromClient) {
//                            ViewManager wm = a.getWindowManager();
//                            View decor = r.window.getDecorView();
//                            wm.updateViewLayout(decor, l);
//                        }
//                    }
//                    r.activity.mVisibleFromServer = true;
//                    mNumVisibleActivities++;
//                    if (r.activity.mVisibleFromClient) {
//                        r.activity.makeVisible();
//                    }
//                }
//
//                r.nextIdle = mNewActivities;
//                mNewActivities = r;
//                if (localLOGV) Slog.v(
//                        TAG, "Scheduling idle handler for " + r);
//                Looper.myQueue().addIdleHandler(new Idler());
//
//            } else {
//                // If an exception was thrown when trying to resume, then
//                // just end this activity.
//                try {
//                    ActivityManagerNative.getDefault()
//                            .finishActivity(token, Activity.RESULT_CANCELED, null);
//                } catch (RemoteException ex) {
//                }
//            }
//        }
//
//        private int mThumbnailWidth = -1;
//        private int mThumbnailHeight = -1;
//
//        private final Bitmap createThumbnailBitmap (ActivityClientRecord r){
//            Bitmap thumbnail = null;
//            try {
//                int w = mThumbnailWidth;
//                int h;
//                if (w < 0) {
//                    Resources res = r.activity.getResources();
//                    mThumbnailHeight = h =
//                            res.getDimensionPixelSize(com.android.internal.R.dimen.thumbnail_height);
//
//                    mThumbnailWidth = w =
//                            res.getDimensionPixelSize(com.android.internal.R.dimen.thumbnail_width);
//                } else {
//                    h = mThumbnailHeight;
//                }
//
//                // On platforms where we don't want thumbnails, set dims to (0,0)
//                if ((w > 0) && (h > 0)) {
//                    View topView = r.activity.getWindow().getDecorView();
//
//                    // Maximize bitmap by capturing in native aspect.
//                    if (topView.getWidth() >= topView.getHeight()) {
//                        thumbnail = Bitmap.createBitmap(w, h, THUMBNAIL_FORMAT);
//                    } else {
//                        thumbnail = Bitmap.createBitmap(h, w, THUMBNAIL_FORMAT);
//                    }
//
//                    thumbnail.eraseColor(0);
//                    Canvas cv = new Canvas(thumbnail);
//                    if (!r.activity.onCreateThumbnail(thumbnail, cv)) {
//                        thumbnail = null;
//                    }
//                }
//
//            } catch (Exception e) {
//                if (!mInstrumentation.onException(r.activity, e)) {
//                    throw new RuntimeException(
//                            "Unable to create thumbnail of "
//                                    + r.intent.getComponent().toShortString()
//                                    + ": " + e.toString(), e);
//                }
//                thumbnail = null;
//            }
//
//            return thumbnail;
//        }
//
//        private final void handlePauseActivity (IBinder token,boolean finished,
//        boolean userLeaving, int configChanges){
//            ActivityClientRecord r = mActivities.get(token);
//            if (r != null) {
//                //Slog.v(TAG, "userLeaving=" + userLeaving + " handling pause of " + r);
//                if (userLeaving) {
//                    performUserLeavingActivity(r);
//                }
//
//                r.activity.mConfigChangeFlags |= configChanges;
//                Bundle state = performPauseActivity(token, finished, true);
//
//                // Make sure any pending writes are now committed.
//                QueuedWork.waitToFinish();
//
//                // Tell the activity manager we have paused.
//                try {
//                    ActivityManagerNative.getDefault().activityPaused(token, state);
//                } catch (RemoteException ex) {
//                }
//            }
//        }
//
//        final void performUserLeavingActivity (ActivityClientRecord r){
//            mInstrumentation.callActivityOnUserLeaving(r.activity);
//        }
//
//        final Bundle performPauseActivity (IBinder token,boolean finished,
//        boolean saveState){
//            ActivityClientRecord r = mActivities.get(token);
//            return r != null ? performPauseActivity(r, finished, saveState) : null;
//        }
//
//        final Bundle performPauseActivity (ActivityClientRecord r,boolean finished,
//        boolean saveState){
//            if (r.paused) {
//                if (r.activity.mFinished) {
//                    // If we are finishing, we won't call onResume() in certain cases.
//                    // So here we likewise don't want to call onPause() if the activity
//                    // isn't resumed.
//                    return null;
//                }
//                RuntimeException e = new RuntimeException(
//                        "Performing pause of activity that is not resumed: "
//                                + r.intent.getComponent().toShortString());
//                Slog.e(TAG, e.getMessage(), e);
//            }
//            Bundle state = null;
//            if (finished) {
//                r.activity.mFinished = true;
//            }
//            try {
//                // Next have the activity save its current state and managed dialogs...
//                if (!r.activity.mFinished && saveState) {
//                    state = new Bundle();
//                    mInstrumentation.callActivityOnSaveInstanceState(r.activity, state);
//                    r.state = state;
//                }
//                // Now we are idle.
//                r.activity.mCalled = false;
//                mInstrumentation.callActivityOnPause(r.activity);
//                EventLog.writeEvent(LOG_ON_PAUSE_CALLED, r.activity.getComponentName().getClassName());
//                if (!r.activity.mCalled) {
//                    throw new SuperNotCalledException(
//                            "Activity " + r.intent.getComponent().toShortString() +
//                                    " did not call through to super.onPause()");
//                }
//
//            } catch (SuperNotCalledException e) {
//                throw e;
//
//            } catch (Exception e) {
//                if (!mInstrumentation.onException(r.activity, e)) {
//                    throw new RuntimeException(
//                            "Unable to pause activity "
//                                    + r.intent.getComponent().toShortString()
//                                    + ": " + e.toString(), e);
//                }
//            }
//            r.paused = true;
//            return state;
//        }
//
//        final void performStopActivity (IBinder token){
//            ActivityClientRecord r = mActivities.get(token);
//            performStopActivityInner(r, null, false);
//        }
//
//        private static class StopInfo {
//            Bitmap thumbnail;
//            CharSequence description;
//        }
//
//        private final class ProviderRefCount {
//            public int count;
//
//            ProviderRefCount(int pCount) {
//                count = pCount;
//            }
//        }
//
//        private final void performStopActivityInner (ActivityClientRecord r,
//                StopInfo info,boolean keepShown){
//            if (localLOGV) Slog.v(TAG, "Performing stop of " + r);
//            if (r != null) {
//                if (!keepShown && r.stopped) {
//                    if (r.activity.mFinished) {
//                        // If we are finishing, we won't call onResume() in certain
//                        // cases.  So here we likewise don't want to call onStop()
//                        // if the activity isn't resumed.
//                        return;
//                    }
//                    RuntimeException e = new RuntimeException(
//                            "Performing stop of activity that is not resumed: "
//                                    + r.intent.getComponent().toShortString());
//                    Slog.e(TAG, e.getMessage(), e);
//                }
//
//                if (info != null) {
//                    try {
//                        // First create a thumbnail for the activity...
//                        info.thumbnail = createThumbnailBitmap(r);
//                        info.description = r.activity.onCreateDescription();
//                    } catch (Exception e) {
//                        if (!mInstrumentation.onException(r.activity, e)) {
//                            throw new RuntimeException(
//                                    "Unable to save state of activity "
//                                            + r.intent.getComponent().toShortString()
//                                            + ": " + e.toString(), e);
//                        }
//                    }
//                }
//
//                if (!keepShown) {
//                    try {
//                        // Now we are idle.
//                        r.activity.performStop();
//                    } catch (Exception e) {
//                        if (!mInstrumentation.onException(r.activity, e)) {
//                            throw new RuntimeException(
//                                    "Unable to stop activity "
//                                            + r.intent.getComponent().toShortString()
//                                            + ": " + e.toString(), e);
//                        }
//                    }
//                    r.stopped = true;
//                }
//
//                r.paused = true;
//            }
//        }
//
//        private final void updateVisibility (ActivityClientRecord r,boolean show){
//            View v = r.activity.mDecor;
//            if (v != null) {
//                if (show) {
//                    if (!r.activity.mVisibleFromServer) {
//                        r.activity.mVisibleFromServer = true;
//                        mNumVisibleActivities++;
//                        if (r.activity.mVisibleFromClient) {
//                            r.activity.makeVisible();
//                        }
//                    }
//                    if (r.newConfig != null) {
//                        if (DEBUG_CONFIGURATION) Slog.v(TAG, "Updating activity vis "
//                                + r.activityInfo.name + " with new config " + r.newConfig);
//                        performConfigurationChanged(r.activity, r.newConfig);
//                        r.newConfig = null;
//                    }
//                } else {
//                    if (r.activity.mVisibleFromServer) {
//                        r.activity.mVisibleFromServer = false;
//                        mNumVisibleActivities--;
//                        v.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//        }
//
//        private final void handleStopActivity (IBinder token,boolean show, int configChanges){
//            ActivityClientRecord r = mActivities.get(token);
//            r.activity.mConfigChangeFlags |= configChanges;
//
//            StopInfo info = new StopInfo();
//            performStopActivityInner(r, info, show);
//
//            if (localLOGV) Slog.v(
//                    TAG, "Finishing stop of " + r + ": show=" + show
//                            + " win=" + r.window);
//
//            updateVisibility(r, show);
//
//            // Tell activity manager we have been stopped.
//            try {
//                ActivityManagerNative.getDefault().activityStopped(
//                        r.token, info.thumbnail, info.description);
//            } catch (RemoteException ex) {
//            }
//        }
//
//        final void performRestartActivity (IBinder token){
//            ActivityClientRecord r = mActivities.get(token);
//            if (r.stopped) {
//                r.activity.performRestart();
//                r.stopped = false;
//            }
//        }
//
//        private final void handleWindowVisibility (IBinder token,boolean show){
//            ActivityClientRecord r = mActivities.get(token);
//            if (!show && !r.stopped) {
//                performStopActivityInner(r, null, show);
//            } else if (show && r.stopped) {
//                // If we are getting ready to gc after going to the background, well
//                // we are back active so skip it.
//                unscheduleGcIdler();
//
//                r.activity.performRestart();
//                r.stopped = false;
//            }
//            if (r.activity.mDecor != null) {
//                if (Config.LOGV) Slog.v(
//                        TAG, "Handle window " + r + " visibility: " + show);
//                updateVisibility(r, show);
//            }
//        }
//
//        private final void deliverResults (ActivityClientRecord r, List < resultinfo > results){
//            final int N = results.size();
//            for (int i = 0; i < n; :="" a = "" about = "" accordingly. = "" act = "" active = ""
//            activities = "" activity = "=" activity.mactivityinfo.configchanges = ""
//            activity.mcalled = "false;" activity.mconfigchangeflags = "0;"
//            activity.mcurrentconfig = "new" activityclientrecord = "" actually = "" after = ""
//            allactivities = "" already = "" an = "" and = "" another = "" any = ""
//            applicationcontext = "" ar = "it.next();" ar.activity.mfinished = ""
//            ar.activityinfo.name = "" are = "" as = "" assume = "" back = "" be = "" because = ""
//            before = "" bitmap = "" boolean="" bother = "" bundle = "" but = "" c = "" call = ""
//            callback = "" callbacks = "new" calling = "" catch="" cb = "" change = "" changed = ""
//            changedconfig = "=" changes = "" charsequence = "" check = "" child = "" cleanly. = ""
//            component = "intent.getComponent();" componentcallbacks = "" componentname = ""
//            config = "" config:="" configchanges = "0x" " configuration=" " configuration.="
//            " context=" " contexts=" " create=" " current=" " currentintent=" r.activity.mIntent;
//            " currently=" " curseq="
//                    + mResConfiguration.seq + " decide=" " delivering=" " description=" null;
//            " destroy=" " destroyed=" " did=" " diff=" " down=" " else=" " everyone.=" " exception="
//            " failure=" " final=" " finish=" " first:=" " for=" " game=" " gc=" " getting="
//            " going=" " gotten=" " had=" " handle=" " handling=" " has=" " have=" " hidden=" " i="
//            0;
//            " ibinder=" " idle.=" " if=" " implementation.=" " in=" " instanceof=" " int="
//            " intent=" " is=" " it=" " it.=" " its=" " just=" " make=" " manager=" " may="
//            " mconfiguration=" = " merge=" " mocked=" " more=" " most=" " mpendingconfiguration="
//            null;
//            " mresconfiguration=" new " n=" mRelaunchingActivities.size();
//            " need=" " needs=" " new=" " newconfig=" + newConfig);
//            ar.newConfig = newConfig;
//        }
//    }
//}
//        }
//                if(mServices.size()>0){
//                Iterator<service> it=mServices.values().iterator();
//        while(it.hasNext()){
//        callbacks.add(it.next());
//        }
//        }
//synchronized (mProviderMap){
//        if(mLocalProviders.size()>0){
//        Iterator<providerclientrecord> it=mLocalProviders.values().iterator();
//        while(it.hasNext()){
//        callbacks.add(it.next().mLocalProvider);
//        }
//        }
//        }
//final int N=mAllApplications.size();
//        for(int i=0;i<n; i++)=" newseq="+config.seq);
//        return false;
//        }
//        int changes=mResConfiguration.updateFrom(config);
//        DisplayMetrics dm=getDisplayMetricsLocked(true);
//
//        // set it for java, this also affects newly created Resources
//        if(config.locale!=null){
//        Locale.setDefault(config.locale);
//        }
//
//        Resources.updateSystemConfiguration(config,dm);
//
//        ContextImpl.ApplicationPackageManager.configurationChanged();
//        //Slog.i(TAG, " next="" normal="" not="" note="" now="" now.="" null="" of="" onconfigurationchanged="" one...="" only="" or="" our="" out="" over="" participating="" passing="" pause="" pending="" pending.="" performing="" previous="" private="" process="" proper="" public="" r="" r.activity="null;" r.activity.mcalled="false;" r.activity.mconfigchangeflags="" r.activity.mdecor="" r.activity.mfinished="true;" r.hidefornow="" r.lastnonconfigurationchildinstances="r.activity.onRetainNonConfigurationChildInstances();" r.lastnonconfigurationinstance="r.activity.onRetainNonConfigurationInstance();" r.nextidle="null;" r.paused="true;" r.pendingintents="tmp.pendingIntents;" r.pendingresults="tmp.pendingResults;" r.startsnotresumed="tmp.startsNotResumed;" r.state="savedState;" r.stopped="true;" r.token="=" r.window="null;" re="" ready="" really="" recent="" regardless="" relaunch="" relaunching="" remoteexception="" replace="" reporting="" resources="" result="" resultdata="" resultinfo="" results="" resumed="" retain="" return="" ri="results.get(i);" ri.mdata="" right="" running="" runtime="" s="" same="" savedstate="" send="" setting="" shouldchangeconfig="false;" shown.="" skip="" skipping="" so="" some="" started="" static="" stop="" string="" superclass="" supernotcalledexception="" sure="" synchronized="" system="" t="" taken="" tear="" tell="" that="" the="" their="" them="" then="" they="" things="" this="" through="" throw="" thumbnail="createThumbnailBitmap(r);" time="" tmp="=" tmp.createdconfig="" tmp.pendingintents="" tmp.pendingresults="" tmp.token="" to="" token="tmp.token;" top="" try="" type="" unable="" up="" us="" v="" version="" version.="" view="" visible.="" void="" want="" we="" well="" what="" while="" will="" window="" windowmanager="" with="" wm="r.activity.getWindowManager();" wtoken="">> it =
//        mActiveResources.values().iterator();
//        //Iterator<map.entry<string, resources="">>> it =
//        //    mActiveResources.entrySet().iterator();
//        while(it.hasNext()){
//        WeakReference<resources> v=it.next();
//        Resources r=v.get();
//        if(r!=null){
//        if(DEBUG_CONFIGURATION)Slog.v(TAG,"Changing resources "
//        +r+" config to: "+config);
//        r.updateConfiguration(config,dm);
//        //Slog.i(TAG, "Updated app resources " + v.getKey()
//        //        + " " + r + ": " + r.getConfiguration());
//        }else{
//        //Slog.i(TAG, "Removing old resources " + v.getKey());
//        it.remove();
//        }
//        }
//
//        return changes!=0;
//        }
//
//final void handleConfigurationChanged(Configuration config){
//
//        ArrayList<componentcallbacks> callbacks=null;
//
//synchronized (mPackages){
//        if(mPendingConfiguration!=null){
//        if(!mPendingConfiguration.isOtherSeqNewer(config)){
//        config=mPendingConfiguration;
//        }
//        mPendingConfiguration=null;
//        }
//
//        if(config==null){
//        return;
//        }
//
//        if(DEBUG_CONFIGURATION)Slog.v(TAG,"Handle configuration changed: "
//        +config);
//
//        applyConfigurationToResourcesLocked(config);
//
//        if(mConfiguration==null){
//        mConfiguration=new Configuration();
//        }
//        if(!mConfiguration.isOtherSeqNewer(config)){
//        return;
//        }
//        mConfiguration.updateFrom(config);
//
//        callbacks=collectComponentCallbacksLocked(false,config);
//        }
//
//        if(callbacks!=null){
//final int N=callbacks.size();
//        for(int i=0;i<n; --=""1024=""8=""access=""activity=""activityclientrecord=""boolean=""can=""catch=""changed:=""closing=""config=""else=""failed=""failure=""final=""finally=""for=""handle=""haspkginfo="false;"i="packages.length-1;"ibinder=""if=""int=""ioexception=""null=""on=""packages=""path=""pcd.path=""process=""profile=""profilercontroldata=""profiling=""r="="r.activity="="runtimeexception=""the=""this=""try=""void="">=0;i--){
//        //Slog.i(TAG, "Cleaning old package: " + packages[i]);
//        if(!hasPkgInfo){
//        WeakReference<loadedapk> ref;
//        ref=mPackages.get(packages[i]);
//        if(ref!=null&&ref.get()!=null){
//        hasPkgInfo=true;
//        }else{
//        ref=mResourcePackages.get(packages[i]);
//        if(ref!=null&&ref.get()!=null){
//        hasPkgInfo=true;
//        }
//        }
//        }
//        mPackages.remove(packages[i]);
//        mResourcePackages.remove(packages[i]);
//        }
//        }
//        ContextImpl.ApplicationPackageManager.handlePackageBroadcast(cmd,packages,
//        hasPkgInfo);
//        }
//
//final void handleLowMemory(){
//        ArrayList<componentcallbacks> callbacks
//        =new ArrayList<componentcallbacks>();
//
//synchronized (mPackages){
//        callbacks=collectComponentCallbacksLocked(true,null);
//        }
//
//final int N=callbacks.size();
//        for(int i=0;i<n; 1024=""8="":=""a=""access=""after=""analysis.=""and=""app=""appbinddata=""appcontext="new"application=""applicationinfo=""applicationinfo.flag_system=""applications=""as=""ask=""assumed=""backup=""base=""be=""because=""before=""being=""bring=""caches.=""can=""catch=""change=""changed=""changes.=""cl="instrContext.getClassLoader();"class.=""compatibility=""configuration=""contextimpl=""could=""data.appinfo.flags=""data.debugmode=""data.handlingprofiling="true;"data.info="getPackageInfoNoCheck(data.appInfo);"data.instrumentationname=""data.profilefile=""date=""debugged=""debugger=""default=""density=""disk=""do=""doing=""done=""dropbox=""else=""eng=""environment=""exception=""file=""final=""find=""font=""for=""for:=""free=""from=""full=""graphics=""have=""iactivitymanager=""if=""ii="="image=""in=""incorrect=""info=""initialize=""instantiate=""instrapp="new"instrapp.datadir="ii.dataDir;"instrapp.nativelibrarydir="ii.nativeLibraryDir;"instrapp.packagename="ii.packageName;"instrapp.publicsourcedir="ii.publicSourceDir;"instrapp.sourcedir="ii.sourceDir;"instrcontext="new"instrumentation=""instrumentationinfo=""int=""is=""it=""its=""java.lang.classloader=""launched=""loadedapk=""locale=""log=""mboundapplication="data;"mconfiguration="new"memory=""mgr="ActivityManagerNative.getDefault();"might=""minitialapplication="app;"minstrumentation="(Instrumentation)"minstrumentationappdir="ii.sourceDir;"minstrumentationapppackage="ii.packageName;"minstrumentedappdir="data.info.getAppDir();"mode=""mostly=""much=""needed.=""needs=""network=""new=""not=""object=""of=""on=""option=""or=""packagemanager.namenotfoundexception=""page=""passed=""pi="getPackageInfo(instrApp,"port=""port.=""possible=""preloaded=""private=""process=""process.=""providerinfo=""reasons=""reflect=""remoteexception=""reset=""restricted=""safely=""send=""set=""should=""since=""spawning=""sqlite=""sqlitereleased="SQLiteDatabase.releaseMemory();"stack=""switch=""system=""the=""this=""throw=""thrown=""time=""to=""traces=""try=""unable=""up=""update=""userdebug=""void=""waiting=""we=""with=""without=""would=""xxx=""zone=""zone.="">providers=data.providers;
//        if(providers!=null){
//        installContentProviders(app,providers);
//        // For process that contain content providers, we want to
//        // ensure that the JIT is enabled "at some point".
//        mH.sendEmptyMessageDelayed(H.ENABLE_JIT,10*1000);
//        }
//
//        try{
//        mInstrumentation.callApplicationOnCreate(app);
//        }catch(Exception e){
//        if(!mInstrumentation.onException(app,e)){
//        throw new RuntimeException(
//        "Unable to create application "+app.getClass().getName()
//        +": "+e.toString(),e);
//        }
//        }
//        }
//
///*package*/ final void finishInstrumentation(int resultCode,Bundle results){
//        IActivityManager am=ActivityManagerNative.getDefault();
//        if(mBoundApplication.profileFile!=null&&mBoundApplication.handlingProfiling){
//        Debug.stopMethodTracing();
//        }
//        //Slog.i(TAG, "am: " + ActivityManagerNative.getDefault()
//        //      + ", app thr: " + mAppThread);
//        try{
//        am.finishInstrumentation(mAppThread,resultCode,results);
//        }catch(RemoteException ex){
//        }
//        }
//
//private final void installContentProviders(
//        Context context,List<providerinfo> providers){
//final ArrayList<iactivitymanager.contentproviderholder>results=
//        new ArrayList<iactivitymanager.contentproviderholder>();
//
//        Iterator<providerinfo> i=providers.iterator();
//        while(i.hasNext()){
//        ProviderInfo cpi=i.next();
//        StringBuilder buf=new StringBuilder(128);
//        buf.append("Pub ");
//        buf.append(cpi.authority);
//        buf.append(": ");
//        buf.append(cpi.name);
//        Log.i(TAG,buf.toString());
//        IContentProvider cp=installProvider(context,null,cpi,false);
//        if(cp!=null){
//        IActivityManager.ContentProviderHolder cph=
//        new IActivityManager.ContentProviderHolder(cpi);
//        cph.provider=cp;
//        results.add(cph);
//// Don't ever unload this provider from the process.
//synchronized(mProviderMap){
//        mProviderRefCountMap.put(cp.asBinder(),new ProviderRefCount(10000));
//        }
//        }
//        }
//
//        try{
//        ActivityManagerNative.getDefault().publishContentProviders(
//        getApplicationThread(),results);
//        }catch(RemoteException ex){
//        }
//        }
//
//private final IContentProvider getExistingProvider(Context context,String name){
//synchronized(mProviderMap){
//final ProviderClientRecord pr=mProviderMap.get(name);
//        if(pr!=null){
//        return pr.mProvider;
//        }
//        return null;
//        }
//        }
//
//private final IContentProvider getProvider(Context context,String name){
//        IContentProvider existing=getExistingProvider(context,name);
//        if(existing!=null){
//        return existing;
//        }
//
//        IActivityManager.ContentProviderHolder holder=null;
//        try{
//        holder=ActivityManagerNative.getDefault().getContentProvider(
//        getApplicationThread(),name);
//        }catch(RemoteException ex){
//        }
//        if(holder==null){
//        Slog.e(TAG,"Failed to find provider info for "+name);
//        return null;
//        }
//
//        IContentProvider prov=installProvider(context,holder.provider,
//        holder.info,true);
//        //Slog.i(TAG, "noReleaseNeeded=" + holder.noReleaseNeeded);
//        if(holder.noReleaseNeeded||holder.provider==null){
//// We are not going to release the provider if it is an external
//// provider that doesn't care about being released, or if it is
//// a local provider running in this process.
////Slog.i(TAG, "*** NO RELEASE NEEDED");
//synchronized(mProviderMap){
//        mProviderRefCountMap.put(prov.asBinder(),new ProviderRefCount(10000));
//        }
//        }
//        return prov;
//        }
//
//public final IContentProvider acquireProvider(Context c,String name){
//        IContentProvider provider=getProvider(c,name);
//        if(provider==null)
//        return null;
//        IBinder jBinder=provider.asBinder();
//synchronized(mProviderMap){
//        ProviderRefCount prc=mProviderRefCountMap.get(jBinder);
//        if(prc==null){
//        mProviderRefCountMap.put(jBinder,new ProviderRefCount(1));
//        }else{
//        prc.count++;
//        } //end else
//        } //end synchronized
//        return provider;
//        }
//
//public final IContentProvider acquireExistingProvider(Context c,String name){
//        IContentProvider provider=getExistingProvider(c,name);
//        if(provider==null)
//        return null;
//        IBinder jBinder=provider.asBinder();
//synchronized(mProviderMap){
//        ProviderRefCount prc=mProviderRefCountMap.get(jBinder);
//        if(prc==null){
//        mProviderRefCountMap.put(jBinder,new ProviderRefCount(1));
//        }else{
//        prc.count++;
//        } //end else
//        } //end synchronized
//        return provider;
//        }
//
//public final boolean releaseProvider(IContentProvider provider){
//        if(provider==null){
//        return false;
//        }
//        IBinder jBinder=provider.asBinder();
//synchronized(mProviderMap){
//        ProviderRefCount prc=mProviderRefCountMap.get(jBinder);
//        if(prc==null){
//        if(localLOGV)Slog.v(TAG,"releaseProvider::Weird shouldn't be here");
//        return false;
//        }else{
//        prc.count--;
//        if(prc.count==0){
//        // Schedule the actual remove asynchronously, since we
//        // don't know the context this will be called in.
//        // TODO: it would be nice to post a delayed message, so
//        // if we come back and need the same provider quickly
//        // we will still have it available.
//        Message msg=mH.obtainMessage(H.REMOVE_PROVIDER,provider);
//        mH.sendMessage(msg);
//        } //end if
//        } //end else
//        } //end synchronized
//        return true;
//        }
//
//final void completeRemoveProvider(IContentProvider provider){
//        IBinder jBinder=provider.asBinder();
//        String name=null;
//synchronized(mProviderMap){
//        ProviderRefCount prc=mProviderRefCountMap.get(jBinder);
//        if(prc!=null&&prc.count==0){
//        mProviderRefCountMap.remove(jBinder);
//        //invoke removeProvider to dereference provider
//        name=removeProviderLocked(provider);
//        }
//        }
//
//        if(name!=null){
//        try{
//        if(localLOGV)Slog.v(TAG,"removeProvider::Invoking "+
//        "ActivityManagerNative.removeContentProvider("+name);
//        ActivityManagerNative.getDefault().removeContentProvider(
//        getApplicationThread(),name);
//        }catch(RemoteException e){
//        //do nothing content provider object is dead any way
//        } //end catch
//        }
//        }
//
//public final String removeProviderLocked(IContentProvider provider){
//        if(provider==null){
//        return null;
//        }
//        IBinder providerBinder=provider.asBinder();
//
//        String name=null;
//
//        // remove the provider from mProviderMap
//        Iterator<providerclientrecord> iter=mProviderMap.values().iterator();
//        while(iter.hasNext()){
//        ProviderClientRecord pr=iter.next();
//        IBinder myBinder=pr.mProvider.asBinder();
//        if(myBinder==providerBinder){
//        //find if its published by this process itself
//        if(pr.mLocalProvider!=null){
//        if(localLOGV)Slog.i(TAG,"removeProvider::found local provider returning");
//        return name;
//        }
//        if(localLOGV)Slog.v(TAG,"removeProvider::Not local provider Unlinking "+
//        "death recipient");
//        //content provider is in another process
//        myBinder.unlinkToDeath(pr,0);
//        iter.remove();
//        //invoke remove only once for the very first name seen
//        if(name==null){
//        name=pr.mName;
//        }
//        } //end if myBinder
//        }  //end while iter
//
//        return name;
//        }
//
//final void removeDeadProvider(String name,IContentProvider provider){
//synchronized(mProviderMap){
//        ProviderClientRecord pr=mProviderMap.get(name);
//        if(pr.mProvider.asBinder()==provider.asBinder()){
//        Slog.i(TAG,"Removing dead content provider: "+name);
//        ProviderClientRecord removed=mProviderMap.remove(name);
//        if(removed!=null){
//        removed.mProvider.asBinder().unlinkToDeath(removed,0);
//        }
//        }
//        }
//        }
//
//final void removeDeadProviderLocked(String name,IContentProvider provider){
//        ProviderClientRecord pr=mProviderMap.get(name);
//        if(pr.mProvider.asBinder()==provider.asBinder()){
//        Slog.i(TAG,"Removing dead content provider: "+name);
//        ProviderClientRecord removed=mProviderMap.remove(name);
//        if(removed!=null){
//        removed.mProvider.asBinder().unlinkToDeath(removed,0);
//        }
//        }
//        }
//
//private final IContentProvider installProvider(Context context,
//        IContentProvider provider,ProviderInfo info,boolean noisy){
//        ContentProvider localProvider=null;
//        if(provider==null){
//        if(noisy){
//        Slog.d(TAG,"Loading provider "+info.authority+": "
//        +info.name);
//        }
//        Context c=null;
//        ApplicationInfo ai=info.applicationInfo;
//        if(context.getPackageName().equals(ai.packageName)){
//        c=context;
//        }else if(mInitialApplication!=null&&
//        mInitialApplication.getPackageName().equals(ai.packageName)){
//        c=mInitialApplication;
//        }else{
//        try{
//        c=context.createPackageContext(ai.packageName,
//        Context.CONTEXT_INCLUDE_CODE);
//        }catch(PackageManager.NameNotFoundException e){
//        }
//        }
//        if(c==null){
//        Slog.w(TAG,"Unable to get context for package "+
//        ai.packageName+
//        " while loading content provider "+
//        info.name);
//        return null;
//        }
//        try{
//    final java.lang.ClassLoader cl=c.getClassLoader();
//        localProvider=(ContentProvider)cl.
//        loadClass(info.name).newInstance();
//        provider=localProvider.getIContentProvider();
//        if(provider==null){
//        Slog.e(TAG,"Failed to instantiate class "+
//        info.name+" from sourceDir "+
//        info.applicationInfo.sourceDir);
//        return null;
//        }
//        if(Config.LOGV)Slog.v(
//        TAG,"Instantiating local provider "+info.name);
//        // XXX Need to create the correct context for this provider.
//        localProvider.attachInfo(c,info);
//        }catch(java.lang.Exception e){
//        if(!mInstrumentation.onException(null,e)){
//        throw new RuntimeException(
//        "Unable to get provider "+info.name
//        +": "+e.toString(),e);
//        }
//            return null;
//        }
//        }else if(localLOGV){
//            Slog.v(TAG,"Installing external provider "+info.authority+": "
//            +info.name);
//        }
//
//    synchronized (mProviderMap){
//        // Cache the pointer for the remote provider.
//        String names[]=PATTERN_SEMICOLON.split(info.authority);
//        for(int i=0;i<names.length;--=""about=""activitythread=""actually=""an=""app="Instrumentation.newApplication(Application.class,"application=""apply=""be=""because=""boolean=""catch=""change=""changed=""configuration=""context="new"contextimpl=""die=""die.=""display=""else=""everyone=""exception=""final=""here=""hierarchy=""iactivitymanager=""if=""informed=""instantiate=""it.=""just=""list=""localprovider=""mgr="ActivityManagerNative.getDefault();"minitialapplication="app;"minstrumentation="new"mpendingconfiguration="newConfig;"msystemthread="system;"need=""new=""null=""object=""pr="new"pre-initialized=""private=""providerclientrecord=""providerinfo=""providers=""public=""remoteexception=""resources=""return=""returning=""set=""static=""synchronized=""system=""t=""tell=""the=""this=""thread="new"throw=""to=""try=""unable=""upon=""view=""void=""want=""we=""will="">)providers);
//        }
//        }
//
//    public static final void main(String[]args){
//        SamplingProfilerIntegration.start();
//
//        Process.setArgV0("<pre-initialized>");
//
//        Looper.prepareMainLooper();
//        if(sMainThreadHandler==null){
//            sMainThreadHandler=new Handler();
//        }
//
//        ActivityThread thread=new ActivityThread();
//        thread.attach(false);
//
//        if(false){
//            Looper.myLooper().setMessageLogging(new
//            LogPrinter(Log.DEBUG,"ActivityThread"));
//        }
//
//        Looper.loop();
//
//        if(Process.supportsProcesses()){
//            throw new RuntimeException("Main thread loop unexpectedly exited");
//        }
//
//        thread.detach();
//        String name=(thread.mInitialApplication!=null)?thread.mInitialApplication.getPackageName():"<unknown>";
//        Slog.i(TAG,"Main thread of "+name+" is now exiting");
//    }
//}