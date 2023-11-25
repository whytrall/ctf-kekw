import datetime
import hashlib
import hmac
import requests

# Your AWS credentials
access_key = "ASIASEF4EPOOQOPYR77F"
secret_key = "ndyFt7znHn2paHsWYsalPfV9XqWCdTx6QhMZy4gF"
# token = "Your Session Token Here"
token = "IQoJb3JpZ2luX2VjEB0aDGV1LWNlbnRyYWwtMSJGMEQCIG1YEVlEVoDBzUmCrjZisSeb+pregnADrRhO+KLaF3y3AiB6nt5dIdb5D4xdhVvBWedeql0B6O5U5MgUoI2m+q962irQBAh2EAEaDDE0NjQyMzQ0NjQyOSIMi9yLIQ49dVF+kI0WKq0E6Bx1QSVCtd4E82yYNu6PC6/jM/bp58mT6G/JhjszqSYHZjqgaGsQEi65RcHpMunr0ox47wYDJweetu2CuJWE9NuCCZMtJMzBlhLvjrGvYwJ4m6VdMhdsaOwkTMvkn94N1NzlQL9MyIPUg+3PPOYNv2k9QZf/X3gPCsJOvtZj3/lV5jcauHHHfDa/Pcgwb1Lpv53CJ9iY3JNP4svi39zcoaB5r3nPDr/wW63H8vC6D8FibTGu17Ewe13N1a91/zmpSMDnXZ0NBNImF0FZCzUiBXa1orQ1pTd5R0hTpnVP57UgcGZyaBnvd84XWSwWA/naMdw/UWUng3VHPWLWSnuwUvzVCl4sMCjF0OGI94VXQnWSM/vF26eXGgPvU/8sEAPKooFN1y4Ots0McO8FCBWZE72ME7HSA3mlt51JZxDv2vYx0cNu6FLdAWFjbZBwdBrBoVSiny8GkOgrD1ju0rK0/2hMD1XPyYNpXCXUkfC8zGyolVwzP/NDbPYGz0BSSvkzTVOZvXwlErYGPVhXimj1iUwKpGbV+0j2HIudgoruQ3miVCOr9RWoNVNW+JIAekqwuQxidh7slTHHguigybgsZBKbEspimn1kJArxcyPVZSMKNxHZZH1oatwduv1N2DmVTs/XLFC+PqiVyH78j+cmdNzLM14vdkHj2kmWDnCrq4FkZThedFOTkZWf69gKN26ZTD5lB6qE0zzqp5ZGKicSKqMKQUxzyeQoD1nuGmAwst+HqwY6lALpksu5RR6qrY65AfEHTxVn9wIyPaj4Pr5XffMwyBZyPNF9D6k2YQErx1GR6QJhBbKD1VxkWe1fpTLtcdM/ykcv7C4TqGLWI4h6/9INutgFNh/2pBwnXVkhx2DGOl7CLfJOyaerslKZUULBGa7ci9om35ZiuQ6z03riqZw9onZRhW+BiX4TuY5y99whXGM5m4TcKnHbk68wWHRhro8r6wmeDXlhsDy191DHeriErGsj90Fk/gCUzsDPIPw16LPHTYBiw/sIt8CMFMVB/owa/Ukk9swIylF0WjFXbQ+DqHvbnFC3uN+FbsRBEp4gMiLMnES8wKAsilu06VeWTERsPpJXoH4LNtTvxQ1G0kPY9DXsKKFbMLc="
region = "eu-central-1"
service = "s3"
host = "marketplace-plugins.s3.amazonaws.com"
bucket = "marketplace-plugins"

# Helper functions for signing requests
def sign(key, msg):
    return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).digest()

def get_signature_key(key, date_stamp, region_name, service_name):
    k_date = sign(('AWS4' + key).encode('utf-8'), date_stamp)
    k_region = sign(k_date, region_name)
    k_service = sign(k_region, service_name)
    k_signing = sign(k_service, 'aws4_request')
    return k_signing

# Create a date for headers and the credential string
t = datetime.datetime.utcnow()
amzdate = t.strftime('%Y%m%dT%H%M%SZ')
date_stamp = t.strftime('%Y%m%d')  # Date w/o time, used in credential scope

# Create canonical URI--the part of the URI from domain to query
canonical_uri = '/'

# Create the canonical headers and signed headers. Header names must be trimmed
# and lowercase, and sorted in code point order from low to high.
canonical_headers = (
    'host:' + host + '\n' +
    'x-amz-content-sha256:UNSIGNED-PAYLOAD\n' +
    'x-amz-date:' + amzdate + '\n' +
    'x-amz-security-token:' + token + '\n'
)
signed_headers = 'host;x-amz-content-sha256;x-amz-date;x-amz-security-token'

# Create payload hash (hash of the request body content). For GET requests, the payload is an empty string.
payload_hash = hashlib.sha256(''.encode('utf-8')).hexdigest()

# Combine elements to create canonical request
canonical_request = 'GET\n' + canonical_uri + '\n' + '\n' + canonical_headers + '\n' + signed_headers + '\n' + payload_hash

# Create the string to sign
algorithm = 'AWS4-HMAC-SHA256'
credential_scope = date_stamp + '/' + region + '/' + service + '/' + 'aws4_request'
string_to_sign = algorithm + '\n' +  amzdate + '\n' +  credential_scope + '\n' +  hashlib.sha256(canonical_request.encode('utf-8')).hexdigest()

# Calculate the signature
signing_key = get_signature_key(secret_key, date_stamp, region, service)
signature = hmac.new(signing_key, string_to_sign.encode('utf-8'), hashlib.sha256).hexdigest()

# Combine elements to create authorization header
authorization_header = (
    algorithm + ' ' +
    'Credential=' + access_key + '/' + credential_scope + ', ' +
    'SignedHeaders=' + signed_headers + ', ' +
    'Signature=' + signature
)

# The 'requests' package handles all the HTTP details
headers = {
    'Authorization': authorization_header,
    'x-amz-date': amzdate,
    'x-amz-content-sha256': 'UNSIGNED-PAYLOAD',
    'x-amz-security-token': token
}

# Make the request and display the response
request_url = 'http://' + host + canonical_uri
print('\nBEGIN REQUEST\n')
print('Request URL = ' + request_url)
r = requests.get(request_url, headers=headers)

print('\nRESPONSE\n')
print('Response code: %d\n' % r.status_code)
print(r.text)
