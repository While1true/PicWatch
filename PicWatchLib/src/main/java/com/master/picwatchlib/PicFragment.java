package com.master.picwatchlib;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/3/26.
 */

public class PicFragment extends Fragment {
    private ArrayList<String> urls;
    private int index = 0;
    private TextView indexview;
    private ViewPager viewPager;
    private int color;

    private MyPhotoView.CloseListener listener;

    public static void Go(FragmentActivity activity, ArrayList<String> urls, int index, @NonNull View shared, int backcolor) {
        shared.setTransitionName("pic");
        PicFragment fragment = new PicFragment().setIndex(index)
                .setUrls(urls).setBackgroundColor(backcolor);
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());
        fragment.setSharedElementEnterTransition(new DetailTransition());
        fragment.setSharedElementReturnTransition(new DetailTransition());
        activity.getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment)
                .addSharedElement(shared, "pic")
                .addToBackStack(null)
                .commit();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static class DetailTransition extends TransitionSet {
        public DetailTransition() {
            init();
        }

        // 允许资源文件使用
        public DetailTransition(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds())
                    .addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }


    public PicFragment setBackgroundColor(int color) {
        this.color = color;
        return this;
    }

    public PicFragment setChildCloseListener(MyPhotoView.CloseListener listener) {
        this.listener = listener;
        return this;
    }

    public PicFragment setUrls(ArrayList<String> urls) {
        this.urls = urls;
        return this;
    }

    public PicFragment setIndex(int index) {
        this.index = index;
        return this;
    }

    public List<String> getUrls() {
        return urls;
    }

    public String getSelectedUrl() {
        return urls.get(getIndex());
    }

    public int getIndex() {
        return index;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pic_watch_layout, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (urls != null) {
            outState.putSerializable("urls", urls);
        }
        outState.putInt("index", index);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt("index");
            urls = (ArrayList<String>) savedInstanceState.getSerializable("urls");
        }
        View root = view.findViewById(R.id.root);
        if (color != 0) {
            root.setBackgroundColor(color);
        }
        viewPager = view.findViewById(R.id.viewpager);
        indexview = view.findViewById(R.id.index);
        indexview.setText(String.format("%s/%s", index + 1, urls.size()));
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                EachPicFragment eachPicFragment = new EachPicFragment().setUrl(urls.get(position));
                if (listener != null) {
                    eachPicFragment.setlistener(listener);
                }
                return eachPicFragment;
            }

            @Override
            public int getCount() {
                return urls.size();
            }
        });
        viewPager.setCurrentItem(index, false);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                index = position;
                indexview.setText(String.format("%s/%s", position + 1, urls.size()));
                super.onPageSelected(position);
            }
        });
    }

    public static class EachPicFragment extends Fragment {
        private String url;
        private MyPhotoView.CloseListener listener;

        public EachPicFragment setUrl(String url) {
            this.url = url;
            return this;
        }

        public EachPicFragment setlistener(MyPhotoView.CloseListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("url", url);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.photoview, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (savedInstanceState != null) {
                url = savedInstanceState.getString("url");
            }
            MyPhotoView photoView = view.findViewById(R.id.photoview);
            if (listener != null) {
                photoView.setCloseListener(listener);
            } else {
                photoView.setMinimumScale(0.65f);
            }
            Glide.with(photoView)
                    .load(url)
                    .into(photoView);
        }

    }
}
