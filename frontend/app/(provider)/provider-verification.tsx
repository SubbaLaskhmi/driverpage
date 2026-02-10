import React, { useEffect, useState } from 'react';
import {
  Alert,
  ScrollView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';

const API = 'http://localhost:8080';

type Status = 'PENDING' | 'VERIFIED' | 'REJECTED' | null;

export default function ProviderVerificationScreen() {
  const token =
    typeof window !== 'undefined'
      ? localStorage.getItem('token')
      : null;

  const [businessName, setBusinessName] = useState('');
  const [licenseNumber, setLicenseNumber] = useState('');
  const [documentUrl, setDocumentUrl] = useState('');

  const [status, setStatus] = useState<Status>(null);
  const [remark, setRemark] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  /* ================= LOAD STATUS ================= */
  const loadStatus = async () => {
    try {
      const res = await fetch(
        `${API}/api/provider/verification/status`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) return;

      const data = await res.json();
      setStatus(data.status);
      setRemark(data.adminRemark || null);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadStatus();
  }, []);

  /* ================= SUBMIT ================= */
  const submitVerification = async () => {
    if (!businessName || !licenseNumber || !documentUrl) {
      Alert.alert('Missing details', 'All fields are required');
      return;
    }

    setLoading(true);

    try {
      const res = await fetch(
        `${API}/api/provider/verification`,
        {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            businessName,
            licenseNumber,
            documentUrl,
          }),
        }
      );

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text);
      }

      Alert.alert(
        'Submitted',
        'Verification submitted successfully'
      );

      setBusinessName('');
      setLicenseNumber('');
      setDocumentUrl('');
      loadStatus();
    } catch (err: any) {
      Alert.alert('Error', err.message || 'Submission failed');
    } finally {
      setLoading(false);
    }
  };

  /* ================= STATUS VIEW ================= */
  const renderStatus = () => {
    if (!status) return null;

    if (status === 'PENDING') {
      return (
        <View className="bg-yellow-50 p-4 rounded-2xl mb-6">
          <Text className="font-black text-yellow-700">
            Verification Pending
          </Text>
          <Text className="text-xs text-yellow-600 mt-1">
            Your documents are under admin review.
          </Text>
        </View>
      );
    }

    if (status === 'VERIFIED') {
      return (
        <View className="bg-green-50 p-4 rounded-2xl mb-6">
          <Text className="font-black text-green-700">
            Verification Approved
          </Text>
          <Text className="text-xs text-green-600 mt-1">
            You now have full provider access.
          </Text>
        </View>
      );
    }

    if (status === 'REJECTED') {
      return (
        <View className="bg-red-50 p-4 rounded-2xl mb-6">
          <Text className="font-black text-red-700">
            Verification Rejected
          </Text>
          {remark && (
            <Text className="text-xs text-red-600 mt-1">
              Reason: {remark}
            </Text>
          )}
        </View>
      );
    }
  };

  return (
    <ScrollView className="flex-1 px-6 pt-24 pb-32">
      <Text className="text-3xl font-black mb-4">
        Provider Verification
      </Text>

      {renderStatus()}

      {/* ================= FORM ================= */}
      {status !== 'VERIFIED' && (
        <View className="bg-white rounded-3xl p-6 border border-gray-100">
          <View className="mb-4">
            <Text className="text-xs font-bold text-gray-400 mb-1">
              Business Name
            </Text>
            <TextInput
              value={businessName}
              onChangeText={setBusinessName}
              placeholder="Parking Lot Pvt Ltd"
              className="bg-gray-100 px-4 py-3 rounded-xl"
            />
          </View>

          <View className="mb-4">
            <Text className="text-xs font-bold text-gray-400 mb-1">
              License Number
            </Text>
            <TextInput
              value={licenseNumber}
              onChangeText={setLicenseNumber}
              placeholder="LIC-XXXXX"
              className="bg-gray-100 px-4 py-3 rounded-xl"
            />
          </View>

          <View className="mb-6">
            <Text className="text-xs font-bold text-gray-400 mb-1">
              Document URL
            </Text>
            <TextInput
              value={documentUrl}
              onChangeText={setDocumentUrl}
              placeholder="https://drive.google.com/..."
              className="bg-gray-100 px-4 py-3 rounded-xl"
            />
          </View>

          <TouchableOpacity
            disabled={loading}
            onPress={submitVerification}
            className="bg-indigo-600 py-4 rounded-2xl items-center"
          >
            <Text className="text-white font-black uppercase">
              {loading ? 'Submitting…' : 'Submit Verification'}
            </Text>
          </TouchableOpacity>
        </View>
      )}

      {/* VERIFIED INFO */}
      {status === 'VERIFIED' && (
        <View className="items-center mt-10">
          <Ionicons
            name="checkmark-circle"
            size={64}
            color="#22C55E"
          />
          <Text className="font-black text-lg mt-3">
            Verification Complete
          </Text>
        </View>
      )}
    </ScrollView>
  );
}
