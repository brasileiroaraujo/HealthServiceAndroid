/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\tiago\\eclipse\\workspace\\HealthServiceAndroid\\src\\com\\signove\\health\\service\\HealthServiceAPI.aidl
 */
package com.signove.health.service;
public interface HealthServiceAPI extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.signove.health.service.HealthServiceAPI
{
private static final java.lang.String DESCRIPTOR = "com.signove.health.service.HealthServiceAPI";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.signove.health.service.HealthServiceAPI interface,
 * generating a proxy if needed.
 */
public static com.signove.health.service.HealthServiceAPI asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.signove.health.service.HealthServiceAPI))) {
return ((com.signove.health.service.HealthServiceAPI)iin);
}
return new com.signove.health.service.HealthServiceAPI.Stub.Proxy(obj);
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
case TRANSACTION_ConfigurePassive:
{
data.enforceInterface(DESCRIPTOR);
com.signove.health.service.HealthAgentAPI _arg0;
_arg0 = com.signove.health.service.HealthAgentAPI.Stub.asInterface(data.readStrongBinder());
int[] _arg1;
_arg1 = data.createIntArray();
this.ConfigurePassive(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_GetConfiguration:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.GetConfiguration(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_RequestDeviceAttributes:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.RequestDeviceAttributes(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_Unconfigure:
{
data.enforceInterface(DESCRIPTOR);
com.signove.health.service.HealthAgentAPI _arg0;
_arg0 = com.signove.health.service.HealthAgentAPI.Stub.asInterface(data.readStrongBinder());
this.Unconfigure(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.signove.health.service.HealthServiceAPI
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
@Override public void ConfigurePassive(com.signove.health.service.HealthAgentAPI agt, int[] specs) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((agt!=null))?(agt.asBinder()):(null)));
_data.writeIntArray(specs);
mRemote.transact(Stub.TRANSACTION_ConfigurePassive, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String GetConfiguration(java.lang.String dev) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
mRemote.transact(Stub.TRANSACTION_GetConfiguration, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void RequestDeviceAttributes(java.lang.String dev) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dev);
mRemote.transact(Stub.TRANSACTION_RequestDeviceAttributes, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void Unconfigure(com.signove.health.service.HealthAgentAPI agt) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((agt!=null))?(agt.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_Unconfigure, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_ConfigurePassive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_GetConfiguration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_RequestDeviceAttributes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_Unconfigure = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void ConfigurePassive(com.signove.health.service.HealthAgentAPI agt, int[] specs) throws android.os.RemoteException;
public java.lang.String GetConfiguration(java.lang.String dev) throws android.os.RemoteException;
public void RequestDeviceAttributes(java.lang.String dev) throws android.os.RemoteException;
public void Unconfigure(com.signove.health.service.HealthAgentAPI agt) throws android.os.RemoteException;
}
