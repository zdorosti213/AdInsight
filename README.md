# **AdInsight**

**AdInsight** is a Spring Boot application designed to provide insights into advertisement impressions and clicks. It processes two JSON files — one containing ad impressions and the other tracking ad clicks — to generate summaries that include aggregated impressions, clicks, revenue, and recommended advertisers by application and country.

# **API Docs**

http://localhost:8080/api-doc

## **Features**

- **Impressions, Clicks, and Revenue Summary:**
    - Grouped by `app_id` and `country_code`.
    - Counts total impressions and clicks.
    - Calculates total revenue for each app and country.

- **Advertiser Recommendations:**
    - Provides a list of recommended `advertiser_ids` for each app and country, based on impressions data.

## **Technologies Used**

- **Java 17**
- **Spring Boot**
- **Jackson (for JSON processing)**
- **Lombok (optional for reducing boilerplate code)**

## **Endpoints**

### **1. Impressions, Clicks, and Revenue Summary**

Returns a summary of impressions, clicks, and revenue grouped by `app_id` and `country_code`.

- **URL:** `/api/data/impressions`
- **Method:** `GET`
- **Response:**

```json
[
  {
    "app_id": 1,
    "country_code": "US",
    "impressions": 102,
    "clicks": 12,
    "revenue": 10.2
  },
  {
    "app_id": 2,
    "country_code": "CA",
    "impressions": 54,
    "clicks": 5,
    "revenue": 3.5
  }
]
