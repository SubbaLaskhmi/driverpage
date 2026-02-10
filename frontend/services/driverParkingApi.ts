import BASE_URL from "../constants/api";

/* ================= TYPES ================= */

// 🔹 This MUST match backend response fields
export interface DriverParkingSpot {
  slotId: number;
  parkingName: string;
  location: string;
  pricePerHour: number;
  available: boolean;
  distance: string;
  rating: number;
}

/* ================= API ================= */

export async function fetchNearbyParking(
  token: string
): Promise<DriverParkingSpot[]> {
  const response = await fetch(
    `${BASE_URL}/api/driver/parking/nearby`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );

  if (!response.ok) {
    throw new Error("Failed to fetch nearby parking");
  }

  return response.json();
}
