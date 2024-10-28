import dht
import machine
import urequests
import time
import ujson
from lib.keys import URL

# DH11
temp_sensor = dht.DHT11(machine.Pin(28))  # DHT11 Constructor
# soil sensor
soil_sensor = machine.ADC(27)

min_moisture = 0
max_moisture = 65535


def read_soil():
    try:
        moisture = round((max_moisture - soil_sensor.read_u16()) * 130 / (max_moisture - min_moisture))
        if 0 <= moisture <= 100:
            return moisture  # Return the moisture value directly as an integer
        else:
            return None  # Return None if the value is out of range
    except Exception as error:
        return None  # Return None if an error occurs


def read_DHT():
    temp_sensor.measure()
    temp = temp_sensor.temperature()
    humidity = temp_sensor.humidity()
    return temp, humidity


def send_values(temp, hum, soil):
    print(URL)
    headers = {'content-type': 'application/json'}
    data = {
        "Id": "Raspberry",
        "temperature": temp,
        "humidity": hum,
        "soil": soil if soil is not None else "Error",
    }
    try:
        json_data = ujson.dumps(data)  # Convert dictionary to JSON string
        print(json_data)
        response = urequests.put(URL, data=json_data, headers=headers)
        print(f"HTTP Response Code: {response.status_code}")
        print("Data sent to the server")
        print(response.text)  # Print the response from the server
        response.close()  # Close the connection to the server
    except Exception as e:
        print("Error in sending request: ", e)


def print_values(temp, hum, moisture):
    print("Temperature:", temp, "C")
    print("Humidity:", hum, "%")
    if moisture is not None:
        print(f"Soil Moisture: {moisture}%")
    else:
        print("Soil Moisture: Error reading moisture")
    print("")


while True:
    try:
        temp, hum = read_DHT()
        soil = read_soil()
        send_values(temp, hum, soil)
        print_values(temp, hum, soil)
        time.sleep(10)
    except Exception as e:
        print("Error: ", e)
        time.sleep(10)
