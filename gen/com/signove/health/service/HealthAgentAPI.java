/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\tiago\\eclipse\\workspace\\HealthServiceAndroid\\src\\com\\signove\\health\\service\\HealthAgentAPI.aidl
 */
package com.signove.health.service;
public interface HealthAgentAPI extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.signove.health.service.HealthAgentAPI
{
private static final java.lang.String DESCRIPTOR = "com.signove.health.service.HealthAgentAPI";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.signove.health.service.HealthAgentAPI interface,
 * generating a proxy if needed.
 */
public static com.signove.health.service.HealthAgentAPI asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.signove.health.service.HealthAgentAPI))) {
return ((com.signove.health.service.HealthAgentAPI)iin);
}
return new com.signove.health.service.HealthAgentAPI.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_Connected:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.Connected(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_Associated:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.Associated(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_MeasurementData:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.MeasurementData(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_DeviceAttributes:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.DeviceAttributes(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_Disassociated:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.Disassociated(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_Disconnected:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.Disconnected(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.signove.health.service.HealthAgentAPI
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void Connected(java.lang.String dev, java.lang.String addr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
_data.writeString(addr);
mRemote.transact(Stub.TRANSACTION_Connected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void Associated(java.lang.String dev, java.lang.String xmldata) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
_data.writeString(xmldata);
mRemote.transact(Stub.TRANSACTION_Associated, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void MeasurementData(java.lang.String dev, java.lang.String xmldata) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
_data.writeString(xmldata);
mRemote.transact(Stub.TRANSACTION_MeasurementData, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void DeviceAttributes(java.lang.String dev, java.lang.String xmldata) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
_data.writeString(xmldata);
mRemote.transact(Stub.TRANSACTION_DeviceAttributes, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void Disassociated(java.lang.String dev) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
mRemote.transact(Stub.TRANSACTION_Disassociated, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void Disconnected(java.lang.String dev) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
mRemote.transact(Stub.TRANSACTION_Disconnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_Connected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_Associated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_MeasurementData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_DeviceAttributes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_Disassociated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_Disconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void Connected(java.lang.String dev, java.lang.String addr) throws android.os.RemoteException;
public void Associated(java.lang.String dev, java.lang.String xmldata) throws android.os.RemoteException;
public void MeasurementData(java.lang.String dev, java.lang.String xmldata) throws android.os.RemoteException;
public void DeviceAttributes(java.lang.String dev, java.lang.String xmldata) throws android.os.RemoteException;
public void Disassociated(java.lang.String dev) throws android.os.RemoteException;
public void Disconnected(java.lang.String dev) throws android.os.RemoteException;
}
