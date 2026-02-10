import React, { useEffect, useState } from "react";
import { Ionicons } from "@expo/vector-icons";
import { LinearGradient } from "expo-linear-gradient";
import { useRouter, Href } from "expo-router";
import {
  Alert,
  FlatList,
  StatusBar,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import Animated, { FadeInDown } from "react-native-reanimated";
import AsyncStorage from "@react-native-async-storage/async-storage";

import {
  fetchNearbyParking,
  DriverParkingSpot,
} from "../../services/driverParkingApi";

/* ================= SCREEN ================= */
export default function DriverDashboardScreen() {
  const router = useRouter();

  const [parkingSlots, setParkingSlots] = useState<DriverParkingSpot[]>([]);
  const [loading, setLoading] = useState(true);

  /* ================= FETCH DATA ================= */
  useEffect(() => {
    const loadParking = async () => {
      try {
        const token = await AsyncStorage.getItem("token");

        if (!token) {
          Alert.alert("Session expired", "Please login again", [
            { text: "OK", onPress: () => router.replace("/(driver)") },
          ]);
          return;
        }

        const data = await fetchNearbyParking(token);
        setParkingSlots(data);
      } catch {
        Alert.alert("Error", "Failed to load parking spots");
      } finally {
        setLoading(false);
      }
    };

    loadParking();
  }, [router]);

  /* ================= HEADER ================= */
  const renderHeader = () => (
    <LinearGradient
      colors={["#00B894", "#00CEC9"]}
      start={{ x: 0, y: 0 }}
      end={{ x: 1, y: 1 }}
      className="pt-14 pb-8 px-6 rounded-b-[35px]"
    >
      <View className="flex-row justify-between items-center mb-6">
        <View className="flex-row items-center gap-3">
          <View className="w-12 h-12 bg-white/20 rounded-2xl justify-center items-center">
            <Ionicons name="person" size={24} color="white" />
          </View>
          <View>
            <Text className="text-white/80 text-[10px] uppercase">
              Welcome back
            </Text>
            <Text className="text-white text-xl font-bold">Driver</Text>
          </View>
        </View>

        <TouchableOpacity
          onPress={async () => {
            await AsyncStorage.multiRemove(["token", "role"]);
            router.replace("/(driver)");
          }}
          className="w-10 h-10 bg-white/20 rounded-full justify-center items-center"
        >
          <Ionicons name="log-out-outline" size={22} color="white" />
        </TouchableOpacity>
      </View>
    </LinearGradient>
  );

  /* ================= SLOT CARD ================= */
  const renderSlotItem = ({
    item,
    index,
  }: {
    item: DriverParkingSpot;
    index: number;
  }) => (
    <Animated.View entering={FadeInDown.delay(index * 100)}>
      <TouchableOpacity
        activeOpacity={0.9}
        className={`bg-white rounded-3xl p-5 mb-4 ${
          !item.available ? "opacity-60" : ""
        }`}
      >
        <View className="flex-row justify-between mb-3">
          <View>
            <Text className="text-lg font-bold">{item.parkingName}</Text>
            <View className="flex-row items-center gap-1 mt-1">
              <Ionicons name="location" size={12} color="#636E72" />
              <Text className="text-xs text-gray-500">{item.location}</Text>
            </View>
            <Text className="text-[11px] text-gray-400 mt-1">
              Spot #{item.slotId}
            </Text>
          </View>

          <View
            className={`px-3 py-1 rounded-full ${
              item.available ? "bg-teal-50" : "bg-red-50"
            }`}
          >
            <Text
              className={`text-[10px] font-bold ${
                item.available ? "text-teal-600" : "text-red-500"
              }`}
            >
              {item.available ? "● Available" : "● Full"}
            </Text>
          </View>
        </View>

        <View className="flex-row justify-between items-center border-t pt-3">
          <View className="flex-row gap-3">
            <Text className="text-xs">{item.distance}</Text>
            <Text className="text-xs">⭐ {item.rating}</Text>
          </View>

          <Text className="text-xl font-bold text-teal-600">
            ₹{item.pricePerHour}
            <Text className="text-xs text-gray-400">/hr</Text>
          </Text>
        </View>
      </TouchableOpacity>
    </Animated.View>
  );

  /* ================= MAIN ================= */
  return (
    <View className="flex-1 bg-driver-bg">
      <StatusBar barStyle="light-content" />
      {renderHeader()}

      <View className="flex-1 px-6 pt-6">
        <Text className="font-bold text-lg mb-4">Nearby Spots</Text>

        {loading && (
          <Text className="text-center text-gray-500 mt-10">
            Loading parking spots...
          </Text>
        )}

        {!loading && parkingSlots.length === 0 && (
          <Text className="text-center text-gray-500 mt-10">
            No parking spots available
          </Text>
        )}

        <FlatList
          data={parkingSlots}
          renderItem={renderSlotItem}
          keyExtractor={(item) => String(item.slotId)}
          showsVerticalScrollIndicator={false}
          contentContainerStyle={{ paddingBottom: 120 }}
        />
      </View>

      {/* ================= BOTTOM NAV ================= */}
      <View className="absolute bottom-8 left-6 right-6 bg-white rounded-2xl p-2 flex-row">
        <TouchableOpacity className="flex-1 items-center">
          <Ionicons name="map" size={24} color="#00B894" />
        </TouchableOpacity>

        <TouchableOpacity
          onPress={() => router.push("/(driver)/bookings" as Href)}
          className="flex-1 py-3 items-center"
        >
          <Ionicons name="ticket" size={24} color="#B2BEC3" />
        </TouchableOpacity>

        <TouchableOpacity
          onPress={() => router.push("/(driver)/profile" as Href)}
          className="flex-1 py-3 items-center"
        >
          <Ionicons name="settings-outline" size={24} color="#B2BEC3" />
        </TouchableOpacity>
      </View>
    </View>
  );
}
