package basicmod;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class Enums {
    // 占位 Tag（暂时不使用，避免空指针）
    public static final AbstractCard.CardTags STANCE = null;

    // 判断是否为架势卡（通过 ID 包含 "Stance"）
    public static boolean isStanceCard(AbstractCard card) {
        return card.cardID.contains("Stance");
    }
}
