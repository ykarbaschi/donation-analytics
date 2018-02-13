package repeatdonorreporter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RepeatDonorsGroupTest {
    RepeatDonorsGroup repeatDonorsGroup;
    @Before
    public void setup(){repeatDonorsGroup = new RepeatDonorsGroup(0.01f);}

    @Test
    public void addValueToEmptyList() {
        repeatDonorsGroup.addOrPut(99.45f);
        assertEquals(99.45, repeatDonorsGroup.amounts.get(0), 0.009f);
    }
}
