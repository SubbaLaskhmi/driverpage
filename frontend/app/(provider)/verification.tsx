import React, { useState } from 'react';
import {
  Alert,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { useRouter } from 'expo-router';

const API = 'http://localhost:8080';

export default function ProviderVerificationScreen() {
  const router = useRouter();

  const token =
    typeof window !== 'undefined'
      ? localStorage.getItem('token')
      : null;

  const [businessName, setBusinessName] = useState('');
  const [licenseNumber, setLicenseNumber] = useState('');
  const [documentUrl, setDocumentUrl] = useState('');
  const [loading, setLoading] = useState(false);

  /* ================= SUBMIT ================= */
  const submit = async () => {
    if (!businessName || !licenseNumber || !documentUrl) {
      Alert.alert('Required', 'All fields are mandatory');
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
        throw new Error();
      }

      Alert.alert(
        'Submitted',
        'Verification submitted. Await admin approval.'
      );
      router.replace('/dashboard');
    } catch {
      Alert.alert('Error', 'Submission failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <View className="flex-1 bg-white px-6 pt-24">
      <Text className="text-3xl font-black mb-6">
        Provider Verification
      </Text>

      <TextInput
        placeholder="Business / Parking Area Name"
        value={businessName}
        onChangeText={setBusinessName}
        className="border border-gray-200 rounded-xl px-4 py-3 mb-4"
      />

      <TextInput
        placeholder="License Number"
        value={licenseNumber}
        onChangeText={setLicenseNumber}
        className="border border-gray-200 rounded-xl px-4 py-3 mb-4"
      />

      <TextInput
        placeholder="Document URL (Drive / S3 / Cloudinary)"
        value={documentUrl}
        onChangeText={setDocumentUrl}
        className="border border-gray-200 rounded-xl px-4 py-3 mb-6"
      />

      <TouchableOpacity
        onPress={submit}
        disabled={loading}
        className="bg-indigo-600 py-4 rounded-xl items-center"
      >
        <Text className="text-white font-bold">
          {loading ? 'Submitting…' : 'Submit Verification'}
        </Text>
      </TouchableOpacity>

      <Text className="text-gray-400 text-xs mt-4 text-center">
        Upload document to Drive / Cloud and paste link
      </Text>
    </View>
  );
}
