import React, { useEffect, useState } from 'react';
import {
  Alert,
  ScrollView,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

interface Provider {
  id: number;
  name: string;
  ownerName: string;
  email: string;
  phone: string;
  status: 'pending' | 'approved' | 'suspended';
  verification?: {
    businessName: string;
    licenseNumber: string;
    documentUrl: string;
    status: 'PENDING' | 'VERIFIED' | 'REJECTED';
  };
}

const API = 'http://localhost:8080';

export default function ProvidersScreen() {
  const [providers, setProviders] = useState<Provider[]>([]);
  const [loading, setLoading] = useState(true);

  const token =
    typeof window !== 'undefined'
      ? localStorage.getItem('token')
      : null;

  /* ================= FETCH PROVIDERS ================= */
  const loadProviders = async () => {
    try {
      const res = await fetch(`${API}/api/admin/providers`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error('Failed to load providers');

      const data = await res.json();
      setProviders(data);
    } catch (err) {
      console.error(err);
      Alert.alert('Error', 'Unable to load providers');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProviders();
  }, []);

  /* ================= ACTION HANDLER ================= */
  const handleAction = async (
    providerId: number,
    action: 'approve' | 'reject' | 'suspend' | 'reactivate'
  ) => {
    const remark =
      typeof window !== 'undefined'
        ? window.prompt(`Enter remark for ${action.toUpperCase()}`)
        : null;

    if (remark === null || remark.trim().length < 3) {
      Alert.alert('Remark required', 'Please enter a valid remark');
      return;
    }

    try {
      const res = await fetch(
        `${API}/api/admin/approvals/provider/${providerId}/${action}?remark=${encodeURIComponent(
          remark
        )}`,
        {
          method: 'PUT',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) throw new Error('Action failed');

      Alert.alert('Success', `Provider ${action}d successfully`);
      loadProviders();
    } catch (err) {
      console.error(err);
      Alert.alert('Error', 'Action failed. Try again.');
    }
  };

  if (loading) {
    return (
      <View className="flex-1 justify-center items-center">
        <Text>Loading providers…</Text>
      </View>
    );
  }

  return (
    <ScrollView className="flex-1 px-6 pt-24 pb-32">
      <Text className="text-3xl font-black mb-6">
        Provider Approvals
      </Text>

      {providers.length === 0 && (
        <Text className="text-gray-400">No providers found</Text>
      )}

      {providers.map((p) => (
        <View
          key={p.id}
          className="bg-white rounded-3xl p-6 mb-4 border border-gray-100"
        >
          <Text className="text-lg font-black">{p.ownerName}</Text>
          <Text className="text-gray-400 text-xs">{p.email}</Text>
          <Text className="text-gray-400 text-xs">{p.phone}</Text>

          {p.verification && (
            <View className="mt-4 bg-gray-50 p-4 rounded-xl">
              <Text className="font-bold text-xs uppercase mb-2">
                Verification Details
              </Text>
              <Text className="text-xs">
                Business: {p.verification.businessName}
              </Text>
              <Text className="text-xs">
                License: {p.verification.licenseNumber}
              </Text>
              <Text className="text-xs text-indigo-600">
                Document: {p.verification.documentUrl}
              </Text>
              <Text className="text-xs mt-1">
                Status: {p.verification.status}
              </Text>
            </View>
          )}

          <View className="flex-row gap-3 mt-4">
            {p.status === 'pending' && (
              <>
                <TouchableOpacity
                  onPress={() => handleAction(p.id, 'approve')}
                  className="bg-green-600 px-4 py-2 rounded-xl"
                >
                  <Text className="text-white font-bold">Approve</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  onPress={() => handleAction(p.id, 'reject')}
                  className="bg-red-500 px-4 py-2 rounded-xl"
                >
                  <Text className="text-white font-bold">Reject</Text>
                </TouchableOpacity>
              </>
            )}

            {p.status === 'approved' && (
              <TouchableOpacity
                onPress={() => handleAction(p.id, 'suspend')}
                className="bg-amber-500 px-4 py-2 rounded-xl"
              >
                <Text className="text-white font-bold">Suspend</Text>
              </TouchableOpacity>
            )}

            {p.status === 'suspended' && (
              <TouchableOpacity
                onPress={() => handleAction(p.id, 'reactivate')}
                className="bg-indigo-600 px-4 py-2 rounded-xl"
              >
                <Text className="text-white font-bold">Reactivate</Text>
              </TouchableOpacity>
            )}
          </View>
        </View>
      ))}
    </ScrollView>
  );
}
