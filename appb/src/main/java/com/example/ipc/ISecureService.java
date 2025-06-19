// appB/src/main/aidl/com/example/appb/ISecureService.aidl
package com.example.appb;

interface ISecureService {
    String sendSecureRequest(in String encryptedData);
}