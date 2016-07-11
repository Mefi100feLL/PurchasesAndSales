package com.PopCorp.Purchases.data.callback;

import com.PopCorp.Purchases.data.model.Region;

public interface DialogRegionsCallback {

    void onSelected(Region region);
    void onCancel();
}
