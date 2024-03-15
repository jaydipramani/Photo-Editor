package com.relish.app.assets;

import android.graphics.Bitmap;

import org.wysaid.common.SharedContext;
import org.wysaid.nativePort.CGEImageHandler;

import java.util.ArrayList;
import java.util.List;

public class OverlayFileAsset {
    public static final OverlayCode[] BURN_EFFECTS = {new OverlayCode(""), new OverlayCode("#unpack @krblend cl overlay/burn/burn_1.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_2.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_3.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_4.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_5.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_6.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_7.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_8.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_9.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_10.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_11.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_12.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_13.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_14.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_15.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_16.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_17.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_18.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_19.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_20.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_21.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_22.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_23.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_24.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_25.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_26.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_27.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_28.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_29.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_30.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_31.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_32.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_33.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_34.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_35.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_36.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_37.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_38.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_39.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_40.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_41.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_42.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_43.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_44.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_45.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_46.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_47.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_48.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_49.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_50.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_51.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_52.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_53.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_54.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_55.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_56.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_57.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_58.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_59.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_60.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_61.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_62.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_63.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_64.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_65.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_66.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_67.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_68.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_69.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_70.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_71.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_72.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_73.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_74.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_75.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_76.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_77.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_78.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_79.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_80.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_81.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_82.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_83.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_84.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_85.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_86.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_87.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_88.webp 100"), new OverlayCode("#unpack @krblend cl overlay/burn/burn_89.webp 100")};
    public static final OverlayCode[] COLOR_EFFECTS = {new OverlayCode(""), new OverlayCode("#unpack @krblend hue overlay/color/color_1.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_2.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_3.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_4.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_5.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_6.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_7.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_8.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_9.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_10.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_11.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_12.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_13.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_14.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_15.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_16.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_17.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_18.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_19.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_20.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_21.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_22.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_23.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_24.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_25.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_26.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_27.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_28.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_29.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_30.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_31.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_32.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_33.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_34.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_35.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_36.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_37.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_38.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_39.webp 100"), new OverlayCode("#unpack @krblend hue overlay/color/color_40.webp 100")};
    public static final OverlayCode[] DODGE_EFFECTS = {new OverlayCode(""), new OverlayCode("#unpack @krblend sr overlay/burn/burn_1.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_2.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_3.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_4.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_5.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_6.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_7.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_8.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_9.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_10.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_11.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_12.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_13.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_14.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_15.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_16.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_17.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_18.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_19.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_20.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_21.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_22.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_23.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_24.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_25.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_26.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_27.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_28.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_29.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_30.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_31.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_32.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_33.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_34.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_35.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_36.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_37.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_38.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_39.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_40.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_41.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_42.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_43.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_44.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_45.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_46.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_47.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_48.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_49.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_50.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_51.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_52.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_53.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_54.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_55.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_56.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_57.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_58.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_59.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_60.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_61.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_62.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_63.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_64.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_65.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_66.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_67.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_68.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_69.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_70.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_71.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_72.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_73.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_74.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_75.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_76.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_77.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_78.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_79.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_80.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_81.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_82.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_83.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_84.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_85.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_86.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_87.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_88.webp 100"), new OverlayCode("#unpack @krblend sr overlay/burn/burn_89.webp 100")};
    public static final OverlayCode[] HARDMIX_EFFECTS = {new OverlayCode("#unpack @krblend hm overlay/burn/burn_1.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_2.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_3.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_4.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_5.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_6.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_7.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_8.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_9.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_10.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_11.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_12.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_13.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_14.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_15.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_16.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_17.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_18.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_19.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_20.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_21.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_22.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_23.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_24.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_25.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_26.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_27.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_28.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_29.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_30.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_31.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_32.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_33.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_34.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_35.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_36.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_37.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_38.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_39.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_40.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_41.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_42.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_43.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_44.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_45.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_46.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_47.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_48.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_49.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_50.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_51.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_52.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_53.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_54.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_55.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_56.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_57.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_58.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_59.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_60.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_61.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_62.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_63.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_64.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_65.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_66.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_67.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_68.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_69.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_70.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_71.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_72.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_73.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_74.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_75.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_76.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_77.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_78.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_79.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_80.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_81.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_82.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_83.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_84.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_85.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_86.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_87.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_88.webp 100"), new OverlayCode("#unpack @krblend hm overlay/burn/burn_89.webp 100")};
    public static final OverlayCode[] HUE_EFFECTS = {new OverlayCode(""), new OverlayCode("#unpack @krblend hue overlay/hue/light_1.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_2.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_3.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_4.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_5.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_6.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_7.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_8.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_9.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_10.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_11.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_12.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_13.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_14.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_15.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_16.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_17.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_18.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_19.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_20.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_21.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_22.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_23.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_24.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_25.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_26.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_27.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_28.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_29.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_30.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_31.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_32.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_33.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_34.webp 100"), new OverlayCode("#unpack @krblend hue overlay/hue/light_35.webp 100")};
    public static final OverlayCode[] OVERLAY_EFFECTS = {new OverlayCode(""), new OverlayCode("#unpack @krblend sr overlay/blend/blend_1.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_2.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_3.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_4.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_5.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_6.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_7.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_8.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_9.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_10.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_11.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_12.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_13.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_14.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_15.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_16.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_17.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_18.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_19.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_20.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_21.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_22.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_23.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_24.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_25.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_26.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_27.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_28.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_29.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_30.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_31.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_32.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_33.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_34.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_35.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_36.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_37.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_38.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_39.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_40.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_41.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_42.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_43.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_44.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_45.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_46.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_47.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_48.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_49.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_50.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_51.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_52.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_53.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_54.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_55.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_56.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_57.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_58.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_59.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_60.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_61.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_62.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_63.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_64.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_65.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_66.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_67.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_68.jpg 100"), new OverlayCode("#unpack @krblend sr overlay/blend/blend_69.jpg 100")};

    public static class OverlayCode {
        private String image;

        OverlayCode(String image2) {
            this.image = image2;
        }

        public String getImage() {
            return this.image;
        }

        public void setImage(String image2) {
            this.image = image2;
        }
    }

    public static List<Bitmap> getListBitmapDodgeEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : DODGE_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapOverlayEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : OVERLAY_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapHardmixEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : HARDMIX_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapHueEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : HUE_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapColorEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : COLOR_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }

    public static List<Bitmap> getListBitmapBurnEffect(Bitmap bitmap) {
        ArrayList arrayList = new ArrayList();
        SharedContext sharedContext = SharedContext.create();
        sharedContext.makeCurrent();
        CGEImageHandler cgeImageHandler = new CGEImageHandler();
        cgeImageHandler.initWithBitmap(bitmap);
        for (OverlayCode filterBean : BURN_EFFECTS) {
            cgeImageHandler.setFilterWithConfig(filterBean.getImage());
            cgeImageHandler.processFilters();
            arrayList.add(cgeImageHandler.getResultBitmap());
        }
        sharedContext.release();
        return arrayList;
    }
}
