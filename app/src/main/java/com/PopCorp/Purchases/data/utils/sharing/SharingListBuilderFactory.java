package com.PopCorp.Purchases.data.utils.sharing;

public class SharingListBuilderFactory {

    public static SharingListBuilder getBuilder(int position){
        SharingListBuilder result = null;
        switch (position){
            case 0:
                result = new SharingListAsSMSBuilder();
                break;
            case 1:
                result = new SharingListAsEmailBuilder();
                break;
            case 2:
                result = new SharingListAsTextBuilder();
                break;
        }
        return result;
    }
}
