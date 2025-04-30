import os
import logging
import requests
import json
import random
from flask import Flask, request, jsonify, render_template, session
from models import Message, db

# Set up logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# Create the Flask application
app = Flask(__name__)
app.secret_key = os.environ.get("SESSION_SECRET", "dev_secret_key")

# Configure database
app.config["SQLALCHEMY_DATABASE_URI"] = os.environ.get("DATABASE_URL",
                                                       "sqlite:///chat.db")
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

# Initialize database
db.init_app(app)

# Hugging Face API configuration
HUGGINGFACE_API_KEY = os.environ.get("HUGGINGFACE_API_KEY")
# Using a conversational model that works with the API
HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/facebook/blenderbot-400M-distill"

headers = {"Authorization": f"Bearer {HUGGINGFACE_API_KEY}"}

# Create tables
with app.app_context():
    db.create_all()


@app.route('/')
def index():
    """Render the web interface for chat"""
    return render_template('index.html')


@app.route('/api/chat', methods=['POST'])
def chat():
    """API endpoint to handle chat messages"""
    try:
        data = request.json
        user_message = data.get('message', '')
        session_id = data.get('session_id', 'default')

        if not user_message:
            return jsonify({"error": "No message provided"}), 400

        # Store user message in database
        user_msg = Message(session_id=session_id,
                           content=user_message,
                           is_user=True)
        db.session.add(user_msg)
        db.session.commit()

        # Get conversation history for context
        messages = Message.query.filter_by(session_id=session_id).order_by(
            Message.timestamp).all()

        # Try to use the Hugging Face API
        try:
            # Advanced pregnancy responses - implement our own detailed responses
            # Extract keywords from user message to identify topic
            user_message_lower = user_message.lower()
            ai_message = ""

            # Check for first trimester questions
            if any(kw in user_message_lower for kw in
                   ["first trimester", "early symptoms", "morning sickness"]):
                ai_message = """<p>The first trimester (weeks 1-12) brings many changes! Common symptoms include:</p>
<ul>
    <li><strong>Morning sickness:</strong> Despite the name, nausea can occur at any time of day. Try eating small, frequent meals and staying hydrated. Ginger tea or crackers before getting out of bed can help.</li>
    <li><strong>Fatigue:</strong> Your body is working hard! Rest when you can and don't overcommit yourself. Your energy will likely return in the second trimester.</li>
    <li><strong>Breast tenderness:</strong> Your breasts are preparing for milk production. A supportive bra can help with discomfort.</li>
    <li><strong>Frequent urination:</strong> Your growing uterus puts pressure on your bladder. This is completely normal, but do stay hydrated.</li>
    <li><strong>Food aversions/cravings:</strong> Changing hormones affect your sense of taste and smell. Listen to your body, but aim for balanced nutrition.</li>
</ul>
<p>Remember, every pregnancy is unique - you might experience all, some, or none of these symptoms. Always discuss concerns with your healthcare provider!</p>"""

            # Check for nutrition questions
            elif any(
                    kw in user_message_lower
                    for kw in ["food", "eat", "diet", "nutrition", "vitamin"]):
                ai_message = """<p>Nutrition during pregnancy is so important for both you and your baby! Here's a comprehensive guide:</p>
<ul>
    <li><strong>Folate/Folic Acid:</strong> Crucial for preventing neural tube defects. Sources include leafy greens, fortified cereals, and prenatal vitamins (400-800 mcg daily).</li>
    <li><strong>Iron:</strong> Your blood volume increases during pregnancy. Find iron in lean meats, beans, spinach, and fortified cereals.</li>
    <li><strong>Calcium:</strong> Essential for developing your baby's bones and teeth. Dairy products, fortified plant milks, and leafy greens are excellent sources.</li>
    <li><strong>Protein:</strong> The building block for your baby's growth. Aim for lean meats, eggs, dairy, legumes, or plant-based proteins.</li>
    <li><strong>Omega-3 Fatty Acids:</strong> Critical for baby's brain development. Low-mercury fish (salmon, trout) twice weekly or plant sources like walnuts and flaxseed are recommended.</li>
</ul>
<p><strong>Foods to avoid:</strong></p>
<ul>
    <li>Raw or undercooked meats, eggs, and seafood</li>
    <li>High-mercury fish (shark, swordfish, king mackerel, tilefish)</li>
    <li>Unpasteurized dairy products and juices</li>
    <li>Excessive caffeine (limit to 200mg daily, about one 12oz cup of coffee)</li>
    <li>Alcohol (no safe amount during pregnancy)</li>
</ul>
<p>Staying hydrated is also crucial - aim for 8-10 glasses of water daily. Your healthcare provider might recommend specific supplements based on your individual needs.</p>"""

            # Check for exercise questions
            elif any(kw in user_message_lower
                     for kw in ["exercise", "workout", "activity", "fitness"]):
                ai_message = """<p>Staying active during pregnancy offers numerous benefits! Here's what you should know:</p>
<ul>
    <li><strong>Benefits:</strong> Exercise can reduce backaches, constipation, bloating, help you sleep better, increase energy, improve mood, prevent excess weight gain, and promote muscle tone and strength.</li>
    <li><strong>Recommended activities:</strong> Walking, swimming, stationary cycling, low-impact aerobics, prenatal yoga, and strength training with light weights are all excellent choices.</li>
    <li><strong>Duration and frequency:</strong> Aim for 150 minutes of moderate activity weekly, spread throughout the week. Even 10-minute sessions count!</li>
    <li><strong>Intensity guideline:</strong> Use the "talk test" - you should be able to carry on a conversation while exercising.</li>
</ul>
<p><strong>Activities to avoid:</strong></p>
<ul>
    <li>Contact sports or activities with fall risks (basketball, soccer, skiing, horseback riding)</li>
    <li>Activities requiring extensive balance, especially in the third trimester</li>
    <li>Hot yoga or exercise in very hot, humid environments</li>
    <li>Exercises that involve lying flat on your back after the first trimester</li>
    <li>Scuba diving, skydiving, or high-altitude activities</li>
</ul>
<p>Always check with your healthcare provider before starting or continuing an exercise program during pregnancy. Listen to your body and stop if you experience pain, dizziness, shortness of breath, or any concerning symptoms.</p>"""

            # Check for labor questions
            elif any(
                    kw in user_message_lower for kw in
                ["labor", "birth", "delivery", "contraction", "water break"]):
                ai_message = """<p>Preparing for labor is a significant part of your pregnancy journey! Here's comprehensive information about labor and delivery:</p>
<p><strong>Signs labor is approaching:</strong></p>
<ul>
    <li><strong>Lightening:</strong> When your baby "drops" lower into your pelvis (may happen 2-4 weeks before labor for first pregnancies)</li>
    <li><strong>Cervical changes:</strong> Effacement (thinning) and dilation (opening) of the cervix</li>
    <li><strong>Braxton Hicks contractions:</strong> "Practice" contractions that don't cause cervical changes</li>
    <li><strong>Bloody show:</strong> Pinkish or blood-streaked mucus discharge as your cervix begins to open</li>
    <li><strong>Rupture of membranes:</strong> When your water breaks, which can be a gush or a slow trickle</li>
</ul>
<p><strong>True labor signs:</strong></p>
<ul>
    <li>Contractions that get progressively stronger, longer, and closer together</li>
    <li>Contractions that don't diminish with rest or position changes</li>
    <li>Pain that typically starts in the back and moves to the front</li>
    <li>Continuing contractions despite walking or movement</li>
</ul>
<p><strong>When to call your healthcare provider:</strong></p>
<ul>
    <li>First pregnancy: Contractions 5 minutes apart for 1 hour</li>
    <li>Subsequent pregnancies: Contractions 5-7 minutes apart</li>
    <li>If your water breaks</li>
    <li>Heavy bleeding (more than a normal period)</li>
    <li>Decreased fetal movement</li>
    <li>Severe headache, vision changes, or sudden swelling</li>
</ul>
<p>Remember that birth plans are helpful but flexibility is key. Each labor experience is unique, and sometimes circumstances require adjustments to your ideal birth scenario.</p>"""

            # Baby development questions
            elif any(kw in user_message_lower for kw in [
                    "development", "grow", "baby size", "trimester",
                    "ultrasound", "scan", "kick"
            ]):
                ai_message = """<p>Watching your baby develop is one of the most fascinating aspects of pregnancy! Here's a trimester-by-trimester guide to your baby's growth:</p>
<p><strong>First Trimester (Weeks 1-12):</strong></p>
<ul>
    <li><strong>Weeks 1-4:</strong> Fertilization occurs, the embryo implants in the uterus, and cell differentiation begins</li>
    <li><strong>Weeks 5-8:</strong> Major organs start forming, arm and leg buds appear, and the neural tube (brain and spinal cord) develops</li>
    <li><strong>Weeks 9-12:</strong> External genitalia begin developing, fingers and toes form, and your baby starts making tiny movements (though you can't feel them yet)</li>
    <li>By 12 weeks, your baby is about 2.5 inches long and weighs around 0.5 ounces</li>
</ul>
<p><strong>Second Trimester (Weeks 13-27):</strong></p>
<ul>
    <li><strong>Weeks 13-16:</strong> Baby's facial features develop, skin is transparent, and you might feel first movements ("quickening")</li>
    <li><strong>Weeks 17-20:</strong> Baby's hearing develops, vernix (waxy coating) covers the skin, and an anatomy scan ultrasound typically occurs</li>
    <li><strong>Weeks 21-27:</strong> Baby develops a sleep-wake cycle, fingerprints form, and lungs begin developing surfactant</li>
    <li>By 27 weeks, your baby is about 14 inches long and weighs around 2 pounds</li>
</ul>
<p><strong>Third Trimester (Weeks 28-40):</strong></p>
<ul>
    <li><strong>Weeks 28-31:</strong> Baby's brain develops rapidly, eyes open and close, and body fat increases</li>
    <li><strong>Weeks 32-35:</strong> Baby practices breathing movements, most turn head-down, and fingernails reach fingertips</li>
    <li><strong>Weeks 36-40:</strong> Lungs mature, baby descends into the pelvis, and final weight gain occurs</li>
    <li>By 40 weeks, your baby is typically 19-21 inches long and weighs 7-9 pounds</li>
</ul>
<p>Each baby develops at their own pace, and these are general guidelines. Your healthcare provider will monitor your baby's growth through ultrasounds and measuring your fundal height.</p>"""

            # If we have a response from any of our conditions, use it directly
            if ai_message:
                # Skip API call, use our curated response
                pass
            else:
                # For general pregnancy questions, use the API with enhanced prompt
                payload = {"inputs": user_message}

                logger.debug(f"Sending to Hugging Face: {payload}")

                response = requests.post(HUGGINGFACE_API_URL,
                                         headers=headers,
                                         json=payload,
                                         timeout=15)

                if response.status_code == 200:
                    # Parse AI response
                    result = response.json()
                    logger.debug(f"API response: {result}")

                    # Handle different response formats
                    if isinstance(result, list) and len(result) > 0:
                        ai_message = result[0].get('generated_text', '')
                    elif isinstance(result, dict):
                        ai_message = result.get('generated_text', '')
                    else:
                        ai_message = str(result)

                    # If the answer is too short, enhance it with more detail
                    if len(ai_message.split()) < 30:
                        ai_message = f"""<p>{ai_message}</p>
<p>Let me provide you with more comprehensive information on this topic:</p>
<p>Pregnancy is a unique journey for every woman, and what you're experiencing is both normal and special. I recommend discussing this with your healthcare provider during your next prenatal appointment for personalized advice.</p>
<p>Remember that your well-being is paramount during pregnancy. Ensure you're:</p>
<ul>
    <li>Getting adequate rest</li>
    <li>Staying hydrated with 8-10 glasses of water daily</li>
    <li>Eating a balanced diet rich in fruits, vegetables, and proteins</li>
    <li>Taking your prenatal vitamins regularly</li>
    <li>Managing stress through gentle activities like prenatal yoga or meditation</li>
</ul>
<p>Would you like more specific information about any particular aspect of your pregnancy?</p>"""
                else:
                    # If API fails, use detailed fallback
                    logger.error(
                        f"Hugging Face API error: {response.status_code} - {response.text}"
                    )
                    raise ValueError(
                        f"API returned status code {response.status_code}")

        except Exception as api_error:
            # Fallback responses if API fails
            logger.error(f"Error with Hugging Face API: {str(api_error)}")

            # Pregnancy-specific fallback responses
            fallback_responses = [
                "As your pregnancy companion, I'd like to help. Could you provide more details about your pregnancy stage?",
                "That's an important pregnancy question. Let me gather some reliable information for you.",
                "Many expectant parents ask similar questions. Can you tell me which trimester you're in?",
                "I understand pregnancy can bring many questions. I'm looking for the most up-to-date medical guidance.",
                "Pregnancy is a unique journey for everyone. I'd like to provide personalized advice - could you share more?",
                "I want to give you accurate pregnancy information. Let me check the latest recommendations.",
                "Your pregnancy health is important. I'm consulting reliable medical sources for you.",
                "Many mothers-to-be have similar concerns. I'm preparing helpful information for your pregnancy journey."
            ]

            # Pick a random fallback response
            ai_message = random.choice(fallback_responses)

        # Store AI response in database
        ai_msg = Message(session_id=session_id,
                         content=ai_message,
                         is_user=False)
        db.session.add(ai_msg)
        db.session.commit()

        return jsonify({"message": ai_message, "messageId": ai_msg.id})

    except Exception as e:
        logger.error(f"Error in chat endpoint: {str(e)}")
        return jsonify({"error": str(e)}), 500


@app.route('/api/history', methods=['GET'])
def get_history():
    """Get chat history for a session"""
    try:
        session_id = request.args.get('session_id', 'default')
        messages = Message.query.filter_by(session_id=session_id).order_by(
            Message.timestamp).all()

        history = [{
            "id": msg.id,
            "content": msg.content,
            "is_user": msg.is_user,
            "timestamp": msg.timestamp.isoformat()
        } for msg in messages]

        return jsonify({"history": history})

    except Exception as e:
        logger.error(f"Error getting history: {str(e)}")
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
