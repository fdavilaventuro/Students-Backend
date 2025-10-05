import csv
import requests
import boto3
import io
import time

# --- Configuración ---
# La URL completa de tu API corriendo localmente
API_URL = "http://localhost:8080/estudiantes"
# Datos de tu bucket de S3
S3_BUCKET_NAME = "fabioedv-output-01"
S3_FILE_KEY = "estudiantes.csv"

def ingest_from_s3_to_api():
    """
    Descarga un CSV desde S3 y envía cada fila como un POST a una API local.
    """
    print(f"Iniciando la ingesta desde s3://{S3_BUCKET_NAME}/{S3_FILE_KEY}")

    # 1. Descargar el archivo desde S3 en memoria
    try:
        s3_client = boto3.client('s3')
        s3_response = s3_client.get_object(Bucket=S3_BUCKET_NAME, Key=S3_FILE_KEY)
        
        # Leemos el contenido del archivo como texto
        csv_content = s3_response['Body'].read().decode('utf-8')
        # Usamos io.StringIO para tratar el string como si fuera un archivo
        csvfile = io.StringIO(csv_content)
        print("-> Archivo CSV descargado de S3 correctamente.")

    except Exception as e:
        print(f"!! Error al descargar el archivo de S3: {e}")
        return

    # 2. Leer el CSV y enviar los POSTs
    reader = csv.DictReader(csvfile)
    success_count = 0
    error_count = 0

    for index, row in enumerate(reader):
        payload = {
            "nombres": row.get("nombres"),
            "apellidos": row.get("apellidos"),
            "email": row.get("email"),
            "telefono": row.get("telefono"),
            "pais": row.get("pais")
        }

        print(f"Enviando registro #{index + 1}: {payload['email']}...")

        try:
            response = requests.post(API_URL, json=payload)
            if 200 <= response.status_code < 300:
                print(f"  -> Éxito (Status: {response.status_code})")
                success_count += 1
            else:
                print(f"  -> Error (Status: {response.status_code}): {response.text}")
                error_count += 1
        except requests.exceptions.RequestException as e:
            print(f"  -> Error de conexión: {e}")
            error_count += 1
            print("   Asegúrate de que tu aplicación se esté ejecutando en el puerto 8080.")
            break # Si hay un error de conexión, detenemos el script

        time.sleep(0.05) # Pausa opcional para no saturar la API

    print("\n--- Resumen de la ingesta ---")
    print(f"Registros exitosos: {success_count}")
    print(f"Registros con error: {error_count}")
    print("-----------------------------")


if __name__ == '__main__':
    ingest_from_s3_to_api()