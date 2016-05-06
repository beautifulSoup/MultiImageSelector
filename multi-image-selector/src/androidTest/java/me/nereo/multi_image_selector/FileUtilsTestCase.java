package me.nereo.multi_image_selector;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.nereo.multi_image_selector.utils.FileUtils;

/**
 * Created by kris on 16/5/6.
 */
@RunWith(AndroidJUnit4.class)
public class FileUtilsTestCase  {

    @Test
    public void testIsHidden(){
        String [] notHidden = {
                "/ssss/wwig/swwz/www",
                "/wwwiif/sqq/zzsaw/",
                "/ww/ff.wgg"
        };
        for(String str : notHidden){
            Assert.assertFalse(FileUtils.isHiddenFile(str));
        }
        String [] hidden = {
            "/.ssww/ff",
                "/qqq/.ss/w.ss"
        };
        for(String str : hidden){
            Assert.assertTrue(FileUtils.isHiddenFile(str));
        }
    }

}
