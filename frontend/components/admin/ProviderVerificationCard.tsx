import React from 'react';
import { Text, View, TouchableOpacity, Linking } from 'react-native';

interface Props {
  data: any;
  onApprove: () => void;
  onReject: () => void;
}

export default function ProviderVerificationCard({
  data,
  onApprove,
  onReject,
}: Props) {
  return (
    <View className="bg-white rounded-3xl p-6 mb-4 border border-gray-100">
      <Text className="text-lg font-black">{data.providerName}</Text>
      <Text className="text-gray-400 text-xs">{data.email}</Text>
      <Text className="text-gray-400 text-xs">{data.phone}</Text>

      <View className="mt-4">
        <Text className="text-xs font-bold text-gray-400 uppercase">
          Business Name
        </Text>
        <Text className="font-bold">{data.businessName}</Text>
      </View>

      <View className="mt-2">
        <Text className="text-xs font-bold text-gray-400 uppercase">
          License Number
        </Text>
        <Text className="font-bold">{data.licenseNumber}</Text>
      </View>

      <TouchableOpacity
        onPress={() => Linking.openURL(data.documentUrl)}
        className="mt-4 bg-indigo-50 px-4 py-3 rounded-xl"
      >
        <Text className="text-indigo-600 font-bold text-center">
          View Uploaded Document
        </Text>
      </TouchableOpacity>

      <View className="flex-row gap-3 mt-6">
        <TouchableOpacity
          onPress={onApprove}
          className="flex-1 bg-green-600 py-3 rounded-xl"
        >
          <Text className="text-white font-bold text-center">Approve</Text>
        </TouchableOpacity>

        <TouchableOpacity
          onPress={onReject}
          className="flex-1 bg-red-500 py-3 rounded-xl"
        >
          <Text className="text-white font-bold text-center">Reject</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
