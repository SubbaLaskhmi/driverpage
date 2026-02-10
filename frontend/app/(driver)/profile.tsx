import React, { useEffect, useState } from "react";
import { Ionicons } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import {
  StatusBar,
  Text,
  TouchableOpacity,
  View,
  ActivityIndicator,
  Alert,
} from "react-native";
import * as SecureStore from "expo-secure-store";

import {
  fetchDriverProfile,
  DriverProfile,
} from "../../services/driverProfileApi";

export default function DriverProfileScreen() {
  const router = useRouter();

  const [profile, setProfile] = useState<DriverProfile | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const token = await SecureStore.getItemAsync("driverToken");

        if (!token) {
          throw new Error("No token");
        }

        const data = await fetchDriverProfile(token);
        setProfile(data);
      } catch (err) {
        Alert.alert(
          "Session expired",
          "Please login again",
          [{ text: "OK", onPress: () => router.replace("/(driver)") }]
        );
      } finally {
        setLoading(false);
      }
    };

    loadProfile();
  }, []);

  if (loading) {
    return (
      <View className="flex-1 items-center justify-center bg-white">
        <ActivityIndicator size="large" color="#00B894" />
        <Text className="mt-4 text-gray-500">Loading profile...</Text>
      </View>
    );
  }

  return (
    <View className="flex-1 bg-white px-6 pt-14">
      <StatusBar barStyle="dark-content" />

      {/* Header */}
      <View className="flex-row items-center mb-8">
        <TouchableOpacity onPress={() => router.back()}>
          <Ionicons name="arrow-back" size={24} color="#000" />
        </TouchableOpacity>
        <Text className="text-xl font-bold ml-4">Profile & Settings</Text>
      </View>

      {/* Profile */}
      {profile && (
        <View className="bg-gray-50 rounded-3xl p-6 border border-gray-200 mb-6">
          <View className="items-center mb-4">
            <View className="w-20 h-20 bg-teal-100 rounded-full items-center justify-center mb-3">
              <Ionicons name="person" size={36} color="#00B894" />
            </View>
            <Text className="text-lg font-bold">{profile.name}</Text>
            <Text className="text-gray-500 text-sm">Driver</Text>
          </View>

          <View className="border-t border-gray-200 pt-4">
            <Text className="text-gray-500 text-xs uppercase mb-1">Email</Text>
            <Text className="text-base mb-3">{profile.email}</Text>

            <Text className="text-gray-500 text-xs uppercase mb-1">
              Phone Number
            </Text>
            <Text className="text-base">{profile.phone}</Text>
          </View>
        </View>
      )}

      {/* Logout */}
      <TouchableOpacity
        className="flex-row items-center px-4 py-4"
        onPress={async () => {
          await SecureStore.deleteItemAsync("driverToken");
          router.replace("/(driver)");
        }}
      >
        <Ionicons name="log-out-outline" size={22} color="#E17055" />
        <Text className="ml-4 text-base text-red-500">Logout</Text>
      </TouchableOpacity>
    </View>
  );
}