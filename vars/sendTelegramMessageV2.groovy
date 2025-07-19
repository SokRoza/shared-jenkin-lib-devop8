def call(String message, String token , String chatId) {
    // uppgrade to use Markdown version instead 
    def encodedMessage = URLEncoder.encode(message, "UTF-8")
    sh """
        curl -s -X POST https://api.telegram.org/bot${token}/sendMessage \\
        -d chat_id=${chatId} \\
        -d text="${encodedMessage}" > /dev/null
    """
}
