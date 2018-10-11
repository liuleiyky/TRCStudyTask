package com.liul.trc_study_task;

import com.liul.trc_study_task.bitmap.IInterface;
import com.liul.trc_study_task.bitmap.TestCast;

public class CastTest {

    public int testCast(){
        IInterface iInterface=new TestCast();
        Object object=iInterface;
        TestCast testCast= (TestCast) object;
        return 1;
    }
}
