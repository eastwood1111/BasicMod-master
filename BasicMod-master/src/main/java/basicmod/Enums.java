package basicmod; import com.megacrit.cardcrawl.cards.AbstractCard;
public class Enums
{

public static final AbstractCard.CardTags STANCE = null;

public static boolean isStanceCard(AbstractCard card) { return card.cardID.contains("Stance"); } }
