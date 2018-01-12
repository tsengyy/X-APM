package github.tornaco.xposedmoduletest.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import github.tornaco.xposedmoduletest.model.CommonPackageInfo;
import github.tornaco.xposedmoduletest.util.PinyinComparator;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;
import github.tornaco.xposedmoduletest.xposed.util.PkgUtil;

/**
 * Created by guohao4 on 2017/10/18.
 * Email: Tornaco@163.com
 */

public interface TRKillPackageLoader {

    @NonNull
    List<CommonPackageInfo> loadInstalled(boolean kill);

    class Impl implements TRKillPackageLoader {

        public static TRKillPackageLoader create(Context context) {
            return new Impl(context);
        }

        private Context context;

        private Impl(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public List<CommonPackageInfo> loadInstalled(boolean kill) {

            List<CommonPackageInfo> out = new ArrayList<>();

            XAshmanManager xAshmanManager = XAshmanManager.get();
            if (!xAshmanManager.isServiceAvailable()) return out;

            String[] packages = xAshmanManager.getTRKApps(kill);

            for (String pkg : packages) {
                CommonPackageInfo p = LoaderUtil.constructCommonPackageInfo(context, pkg);
                if (p != null) out.add(p);
            }
            java.util.Collections.sort(out, new RFComparator());

            return out;
        }
    }

    class RFComparator implements Comparator<CommonPackageInfo> {
        public int compare(CommonPackageInfo o1, CommonPackageInfo o2) {
            return new PinyinComparator().compare(o1.getAppName(), o2.getAppName());
        }
    }
}
