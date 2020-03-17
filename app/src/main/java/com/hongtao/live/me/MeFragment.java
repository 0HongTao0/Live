package com.hongtao.live.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hongtao.live.R;
import com.hongtao.live.module.User;
import com.hongtao.live.net.ServiceGenerator;
import com.hongtao.live.util.DateUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created 2020/3/4.
 *
 * @author HongTao
 */
public class MeFragment extends Fragment {
    private static final String TAG = "MeFragment";

    private TextView mTvUserName, mTvUserId, mTvGender, mTvBirthday, mTvJob, mTvAddress, mTvIntroduce, mTvLiveIntroduce;
    private ImageView mIvAvatar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);
        mTvUserName = rootView.findViewById(R.id.me_tv_user_name);
        mTvUserId = rootView.findViewById(R.id.me_tv_user_id);
        mTvGender = rootView.findViewById(R.id.me_tv_gender);
        mTvBirthday = rootView.findViewById(R.id.me_tv_birthday);
        mTvJob = rootView.findViewById(R.id.me_tv_job);
        mTvAddress = rootView.findViewById(R.id.me_tv_address);
        mTvIntroduce = rootView.findViewById(R.id.me_tv_introduce);
        mTvLiveIntroduce = rootView.findViewById(R.id.me_tv_live_introduce);
        mIvAvatar = rootView.findViewById(R.id.me_iv_avatar);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        MeApi meApi = ServiceGenerator.createService(MeApi.class);
        meApi.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: " + user.toString());
                        showUser(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    private void showUser(User user) {
        mTvUserName.setText(user.getNick());
        mTvUserId.setText(user.getUserId());
        mTvGender.setText(user.getGender() == 0 ? "女" : "男");
        mTvBirthday.setText(DateUtil.getDate(user.getBirthday()));
        mTvJob.setText(user.getJob());
        mTvAddress.setText(user.getAddress());
        mTvIntroduce.setText(user.getIntroduction());
        mTvLiveIntroduce.setText(user.getLiveIntroduction());
        Glide.with(mIvAvatar.getContext())
                .load(user.getAvatar())
                .into(mIvAvatar);
    }
}
