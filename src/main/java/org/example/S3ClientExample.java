package org.example;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3ClientExample {
    public static void main(String[] args) {
        String accessKey = "YOUR_ACCESS_KEY";
        String secretKey = "YOUR_SECRET_KEY";
        String sessionToken = "IQoJb3JpZ2luX2VjEB0aDGV1LWNlbnRyYWwtMSJGMEQCIG1YEVlEVoDBzUmCrjZisSeb+pregnADrRhO+KLaF3y3AiB6nt5dIdb5D4xdhVvBWedeql0B6O5U5MgUoI2m+q962irQBAh2EAEaDDE0NjQyMzQ0NjQyOSIMi9yLIQ49dVF+kI0WKq0E6Bx1QSVCtd4E82yYNu6PC6/jM/bp58mT6G/JhjszqSYHZjqgaGsQEi65RcHpMunr0ox47wYDJweetu2CuJWE9NuCCZMtJMzBlhLvjrGvYwJ4m6VdMhdsaOwkTMvkn94N1NzlQL9MyIPUg+3PPOYNv2k9QZf/X3gPCsJOvtZj3/lV5jcauHHHfDa/Pcgwb1Lpv53CJ9iY3JNP4svi39zcoaB5r3nPDr/wW63H8vC6D8FibTGu17Ewe13N1a91/zmpSMDnXZ0NBNImF0FZCzUiBXa1orQ1pTd5R0hTpnVP57UgcGZyaBnvd84XWSwWA/naMdw/UWUng3VHPWLWSnuwUvzVCl4sMCjF0OGI94VXQnWSM/vF26eXGgPvU/8sEAPKooFN1y4Ots0McO8FCBWZE72ME7HSA3mlt51JZxDv2vYx0cNu6FLdAWFjbZBwdBrBoVSiny8GkOgrD1ju0rK0/2hMD1XPyYNpXCXUkfC8zGyolVwzP/NDbPYGz0BSSvkzTVOZvXwlErYGPVhXimj1iUwKpGbV+0j2HIudgoruQ3miVCOr9RWoNVNW+JIAekqwuQxidh7slTHHguigybgsZBKbEspimn1kJArxcyPVZSMKNxHZZH1oatwduv1N2DmVTs/XLFC+PqiVyH78j+cmdNzLM14vdkHj2kmWDnCrq4FkZThedFOTkZWf69gKN26ZTD5lB6qE0zzqp5ZGKicSKqMKQUxzyeQoD1nuGmAwst+HqwY6lALpksu5RR6qrY65AfEHTxVn9wIyPaj4Pr5XffMwyBZyPNF9D6k2YQErx1GR6QJhBbKD1VxkWe1fpTLtcdM/ykcv7C4TqGLWI4h6/9INutgFNh/2pBwnXVkhx2DGOl7CLfJOyaerslKZUULBGa7ci9om35ZiuQ6z03riqZw9onZRhW+BiX4TuY5y99whXGM5m4TcKnHbk68wWHRhro8r6wmeDXlhsDy191DHeriErGsj90Fk/gCUzsDPIPw16LPHTYBiw/sIt8CMFMVB/owa/Ukk9swIylF0WjFXbQ+DqHvbnFC3uN+FbsRBEp4gMiLMnES8wKAsilu06VeWTERsPpJXoH4LNtTvxQ1G0kPY9DXsKKFbMLc=";

        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                accessKey,
                secretKey,
                sessionToken);

        sessionCredentials.

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .build();

        s3Client

        // Use s3Client to make requests to Amazon S3
    }
}