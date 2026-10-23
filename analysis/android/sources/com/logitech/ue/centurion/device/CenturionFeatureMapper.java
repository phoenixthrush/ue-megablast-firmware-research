package com.logitech.ue.centurion.device;

/* JADX INFO: loaded from: classes2.dex */
public class CenturionFeatureMapper implements com.logitech.ue.centurion.device.IFeatureMapper {
    private static final int DEFAULT_TYPE = 0;
    private static final int DEFAULT_VERSION = 1;
    private com.google.common.collect.BiMap<java.lang.Integer, com.logitech.ue.centurion.feature.Feature> mFeatureMap = com.google.common.collect.HashBiMap.create();

    public CenturionFeatureMapper() {
        invalidate();
    }

    @Override // com.logitech.ue.centurion.device.IFeatureMapper
    public void addFeatureMapping(com.logitech.ue.centurion.feature.Feature feature, int i) {
        this.mFeatureMap.put(java.lang.Integer.valueOf(i), (com.logitech.ue.centurion.feature.Feature) com.google.common.base.Preconditions.checkNotNull(feature));
    }

    @Override // com.logitech.ue.centurion.device.IFeatureMapper
    public void removeFeatureMapping(int i) {
        this.mFeatureMap.remove(java.lang.Integer.valueOf(i));
    }

    @Override // com.logitech.ue.centurion.device.IFeatureMapper
    public int getFeatureIndex(int i) {
        for (com.logitech.ue.centurion.feature.Feature feature : this.mFeatureMap.values()) {
            if (feature.getId() == i) {
                return this.mFeatureMap.inverse().get(feature).intValue();
            }
        }
        return -128;
    }

    @Override // com.logitech.ue.centurion.device.IFeatureMapper
    public int getFeatureID(int i) {
        if (this.mFeatureMap.containsKey(java.lang.Integer.valueOf(i))) {
            return this.mFeatureMap.get(java.lang.Integer.valueOf(i)).getId();
        }
        return -128;
    }

    @Override // com.logitech.ue.centurion.device.IFeatureMapper
    public com.logitech.ue.centurion.feature.Feature getFeature(int i) {
        if (getFeatureIndex(i) != -128) {
            return this.mFeatureMap.get(java.lang.Integer.valueOf(getFeatureIndex(i)));
        }
        return null;
    }

    @Override // com.logitech.ue.centurion.device.IFeatureMapper
    public boolean isFeatureSupported(int i) {
        return getFeature(i) != null;
    }

    @Override // com.logitech.ue.centurion.device.IFeatureMapper
    public void invalidate() {
        this.mFeatureMap.clear();
        this.mFeatureMap.put(0, new com.logitech.ue.centurion.feature.Feature(0, 0, 1));
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("[");
        for (java.lang.Integer num : this.mFeatureMap.keySet()) {
            com.logitech.ue.centurion.feature.Feature feature = this.mFeatureMap.get(num);
            sb.append("Name: ");
            sb.append(com.logitech.ue.centurion.feature.Features.getName(feature.getId()));
            sb.append(" ID: ");
            sb.append(feature.getId());
            sb.append(" Index: ");
            sb.append(num);
            sb.append(" Type: ");
            sb.append(feature.getType());
            sb.append(" Version: ");
            sb.append(feature.getVersion());
            sb.append("; ");
        }
        sb.append("]");
        return sb.toString();
    }
}
