package com.hemaapp.xsm;

import com.hemaapp.hm_FrameWork.HemaNetTask;

import java.util.HashMap;

/**
 * Created by lenovo on 2016/9/6.
 */
public abstract  class JhNetTask extends HemaNetTask {

    /**
     * ʵ��������������
     *
     * @param information
     *            ����������Ϣ
     * @param params
     *            �������?������,����ֵ)
     */
    public JhNetTask(JhHttpInformation information,
                     HashMap<String, String> params) {
        this(information, params, null);
    }

    /**
     * ʵ��������������
     *
     * @param information
     *            ����������Ϣ
     * @param params
     *            �������?������,����ֵ)
     * @param files
     *            �����ļ���(������,�ļ��ı���·��)
     */
    public JhNetTask(JhHttpInformation information,
                     HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public JhHttpInformation getHttpInformation() {
        return (JhHttpInformation) super.getHttpInformation();
    }


}
