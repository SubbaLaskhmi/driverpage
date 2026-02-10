import BASE_URL from "../constants/api";

export interface DriverProfile {
  name: string;
  email: string;
  phone: string;
}

export async function fetchDriverProfile(token: string): Promise<DriverProfile> {
  const response = await fetch(`${BASE_URL}/api/driver/profile`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Unauthorized");
  }

  return response.json();
}