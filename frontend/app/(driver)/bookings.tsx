import { Ionicons } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import React from "react";
import {
  FlatList,
  StatusBar,
  Text,
  TouchableOpacity,
  View,
} from "react-native";

interface Booking {
  id: string;
  place: string;
  slot: string;
  date: string;
  status: "ACTIVE" | "COMPLETED" | "CANCELLED";
}

export default function DriverBookingsScreen() {
  const router = useRouter();

  // ✅ Dummy bookings (frontend only for now)
  const bookings: Booking[] = [
    {
      id: "1",
      place: "City Center Hub",
      slot: "A1",
      date: "10 Feb 2026, 10:00 AM",
      status: "ACTIVE",
    },
    {
      id: "2",
      place: "Grand Mall Plaza",
      slot: "B3",
      date: "05 Feb 2026, 2:00 PM",
      status: "COMPLETED",
    },
  ];

  return (
    <View className="flex-1 bg-white px-6 pt-14">
      <StatusBar barStyle="dark-content" />

      {/* Header */}
      <View className="flex-row items-center mb-6">
        <TouchableOpacity onPress={() => router.back()}>
          <Ionicons name="arrow-back" size={24} color="#000" />
        </TouchableOpacity>
        <Text className="text-xl font-bold ml-4">My Bookings</Text>
      </View>

      {/* Booking List */}
      <FlatList
        data={bookings}
        keyExtractor={(item) => item.id}
        showsVerticalScrollIndicator={false}
        renderItem={({ item }) => (
          <View className="bg-gray-50 rounded-2xl p-4 mb-4 border border-gray-200">
            <Text className="text-lg font-bold">{item.place}</Text>
            <Text className="text-gray-500 mt-1">
              Slot: {item.slot}
            </Text>
            <Text className="text-gray-500">
              {item.date}
            </Text>

            <View className="mt-3">
              <Text
                className={`text-xs font-bold uppercase ${
                  item.status === "ACTIVE"
                    ? "text-green-600"
                    : item.status === "COMPLETED"
                    ? "text-blue-600"
                    : "text-red-600"
                }`}
              >
                {item.status}
              </Text>
            </View>
          </View>
        )}
      />
    </View>
  );
}