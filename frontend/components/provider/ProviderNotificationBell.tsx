import { Ionicons } from '@expo/vector-icons';
import React, { useEffect, useState } from 'react';
import {
  Modal,
  Pressable,
  Text,
  TouchableOpacity,
  View,
  ScrollView,
} from 'react-native';

interface Notification {
  id: number;
  message: string;
  read: boolean;
  createdAt: string;
}

const API = 'http://localhost:8080';

export default function ProviderNotificationBell() {
  const [visible, setVisible] = useState(false);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);

  const token =
    typeof window !== 'undefined'
      ? localStorage.getItem('token')
      : null;

  /* ================= LOAD NOTIFICATIONS ================= */
  const loadNotifications = async () => {
    if (!token) return;

    try {
      const res = await fetch(
        `${API}/api/provider/notifications`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) return;

      const data = await res.json();
      setNotifications(data);
    } catch (err) {
      console.error(err);
    }
  };

  const loadUnreadCount = async () => {
    if (!token) return;

    try {
      const res = await fetch(
        `${API}/api/provider/notifications/unread-count`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!res.ok) return;

      const count = await res.json();
      setUnreadCount(count);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadNotifications();
    loadUnreadCount();
  }, [token]);

  /* ================= MARK ALL AS READ ================= */
  const markAllAsRead = async () => {
    if (!token) return;

    const unread = notifications.filter((n) => !n.read);

    try {
      await Promise.all(
        unread.map((n) =>
          fetch(
            `${API}/api/provider/notifications/${n.id}/read`,
            {
              method: 'PUT',
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          )
        )
      );

      // Update UI instantly
      setNotifications((prev) =>
        prev.map((n) => ({ ...n, read: true }))
      );
      setUnreadCount(0);
    } catch (err) {
      console.error(err);
    }
  };

  /* ================= OPEN MODAL ================= */
  const openModal = () => {
    setVisible(true);
    markAllAsRead();
  };

  return (
    <>
      {/* 🔔 BELL ICON */}
      <Pressable onPress={openModal}>
        <Ionicons
          name="notifications-outline"
          size={22}
          color="white"
        />

        {unreadCount > 0 && (
          <View className="absolute -top-1 -right-1 w-2 h-2 bg-red-500 rounded-full" />
        )}
      </Pressable>

      {/* 🔔 MODAL */}
      <Modal visible={visible} transparent animationType="slide">
        <Pressable
          className="flex-1 bg-black/40 justify-end"
          onPress={() => setVisible(false)}
        >
          <Pressable className="bg-white rounded-t-3xl p-6 max-h-[70%]">
            <Text className="text-xl font-black mb-4">
              Notifications
            </Text>

            {notifications.length === 0 && (
              <Text className="text-gray-400">
                No notifications
              </Text>
            )}

            <ScrollView>
              {notifications.map((n) => (
                <View
                  key={n.id}
                  className="border-b border-gray-100 py-4"
                >
                  <Text
                    className={`font-bold ${
                      n.read
                        ? 'text-gray-400'
                        : 'text-black'
                    }`}
                  >
                    {n.message}
                  </Text>
                  <Text className="text-xs text-gray-400 mt-1">
                    {new Date(
                      n.createdAt
                    ).toLocaleString()}
                  </Text>
                </View>
              ))}
            </ScrollView>

            <TouchableOpacity
              onPress={() => setVisible(false)}
              className="mt-6 bg-gray-100 py-3 rounded-xl items-center"
            >
              <Text className="font-bold">Close</Text>
            </TouchableOpacity>
          </Pressable>
        </Pressable>
      </Modal>
    </>
  );
}
