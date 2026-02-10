import React, { useEffect, useState } from 'react';
import {
  Alert,
  Modal,
  ScrollView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
  Linking,
} from 'react-native';

const API = 'http://localhost:8080';

interface Verification {
  verificationId: number;
  providerId: number;
  providerName: string;
  email: string;
  businessName: string;
  licenseNumber: string;
  documentUrl: string;
  submittedAt: string;
  status: string;
}

export default function ProviderVerificationScreen() {
  const [verifications, setVerifications] = useState<Verification[]>([]);
  const [selected, setSelected] = useState<Verification | null>(null);
  const [remark, setRemark] = useState('');
  const [loading, setLoading] = useState(true);

  const token =
    typeof window !== 'undefined'
      ? localStorage.getItem('token')
      : null;

  /* ================= LOAD PENDING VERIFICATIONS ================= */
  const loadVerifications = async () => {
    try {
      const res = await fetch(
        `${API}/api/admin/provider-verifications`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const data = await res.json();
      setVerifications(data);
    } catch (err) {
      Alert.alert('Error', 'Failed to load verifications');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadVerifications();
  }, []);

  /* ================= APPROVE ================= */
  const approve = async () => {
    if (!selected) return;

    try {
      await fetch(
        `${API}/api/admin/approvals/provider/${selected.providerId}/approve?remark=${remark}`,
        {
          method: 'PUT',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      Alert.alert('Approved', 'Provider approved successfully');
      closeModal();
      loadVerifications();
    } catch {
      Alert.alert('Error', 'Approval failed');
    }
  };

  /* ================= REJECT ================= */
  const reject = async () => {
    if (!remark.trim()) {
      Alert.alert('Required', 'Rejection reason is required');
      return;
    }

    try {
      await fetch(
        `${API}/api/admin/approvals/provider/${selected?.providerId}/reject?remark=${remark}`,
        {
          method: 'PUT',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      Alert.alert('Rejected', 'Provider rejected');
      closeModal();
      loadVerifications();
    } catch {
      Alert.alert('Error', 'Rejection failed');
    }
  };

  const closeModal = () => {
    setSelected(null);
    setRemark('');
  };

  if (loading) {
    return (
      <View className="flex-1 justify-center items-center">
        <Text>Loading verifications…</Text>
      </View>
    );
  }

  return (
    <ScrollView className="flex-1 px-6 pt-24 pb-32">
      <Text className="text-3xl font-black mb-6">
        Provider Verifications
      </Text>

      {verifications.length === 0 && (
        <Text className="text-gray-400">
          No pending verifications
        </Text>
      )}

      {verifications.map((v) => (
        <TouchableOpacity
          key={v.verificationId}
          onPress={() => setSelected(v)}
          className="bg-white rounded-3xl p-6 mb-4 border border-gray-100"
        >
          <Text className="text-lg font-black">
            {v.providerName}
          </Text>
          <Text className="text-xs text-gray-400">
            {v.email}
          </Text>

          <View className="mt-3">
            <Text className="text-xs text-gray-500">
              Business: {v.businessName}
            </Text>
            <Text className="text-xs text-gray-500">
              License: {v.licenseNumber}
            </Text>
          </View>
        </TouchableOpacity>
      ))}

      {/* ================= MODAL ================= */}
      <Modal visible={!!selected} transparent animationType="slide">
        <View className="flex-1 bg-black/40 justify-end">
          <View className="bg-white rounded-t-3xl p-6">
            <Text className="text-xl font-black mb-2">
              Verify Provider
            </Text>

            <Text className="font-bold">
              {selected?.providerName}
            </Text>
            <Text className="text-gray-400 text-xs mb-4">
              {selected?.email}
            </Text>

            <TouchableOpacity
              onPress={() =>
                Linking.openURL(selected?.documentUrl || '')
              }
              className="bg-indigo-100 py-3 rounded-xl items-center mb-4"
            >
              <Text className="font-bold text-indigo-600">
                View Uploaded Document
              </Text>
            </TouchableOpacity>

            <TextInput
              placeholder="Admin remark / rejection reason"
              value={remark}
              onChangeText={setRemark}
              className="border border-gray-200 rounded-xl px-4 py-3 mb-4"
            />

            <View className="flex-row gap-3">
              <TouchableOpacity
                onPress={approve}
                className="flex-1 bg-green-600 py-3 rounded-xl items-center"
              >
                <Text className="text-white font-bold">
                  Approve
                </Text>
              </TouchableOpacity>

              <TouchableOpacity
                onPress={reject}
                className="flex-1 bg-red-500 py-3 rounded-xl items-center"
              >
                <Text className="text-white font-bold">
                  Reject
                </Text>
              </TouchableOpacity>
            </View>

            <TouchableOpacity
              onPress={closeModal}
              className="mt-4 py-3 items-center"
            >
              <Text className="text-gray-500 font-bold">
                Cancel
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </ScrollView>
  );
}
