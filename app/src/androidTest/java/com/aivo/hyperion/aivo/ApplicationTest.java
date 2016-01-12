package com.aivo.hyperion.aivo;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.aivo.hyperion.aivo.models.Magnet;
import com.aivo.hyperion.aivo.models.MagnetGroup;
import com.aivo.hyperion.aivo.models.Mindmap;
import com.aivo.hyperion.aivo.models.ModelListener;
import com.aivo.hyperion.aivo.models.ModelMediator;
import com.aivo.hyperion.aivo.models.User;
import com.aivo.hyperion.aivo.models.actions.ActionHandler;
import com.aivo.hyperion.aivo.models.actions.CreateMagnet;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    private MockListener listener1;
    private MockListener listener2;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        listener1 = new MockListener();
        listener2 = new MockListener();


        ModelMediator mediator = new ModelMediator();
        listener1.setMediator(mediator);
        listener2.setMediator(mediator);

        mediator.registerListener(listener1);
        mediator.registerListener(listener2);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }



    public void testCreateUser() throws Exception {
        listener1.getMediator().openUser(-1);
//        wait(1000);
        assertEquals(true, listener1.getMediator().getUser() != null);
        assertEquals(true, listener1.getUser() != null);
        assertEquals(true, listener2.getUser() != null);
        assertEquals(true, listener1.getUser().getId() == listener2.getUser().getId());
    }


    public void testCreateMindmap() throws  Exception {
        testCreateUser();

        listener1.getMediator().openMindmap(-1);
        assertEquals(true, listener1.getMindmap() != null);
        assertEquals(true, listener2.getMindmap() != null);
        assertEquals(true, listener1.getMindmap().getId() == listener2.getMindmap().getId());
        assertEquals(true, listener1.getMindmap().getLines().isEmpty());
        assertEquals(true, listener1.getMindmap().getMagnetGroups().isEmpty());

        assertEquals(true, listener1.getMediator().getMindmap() == listener1.getMindmap());
    }

    public void testCreateMagnet() throws Exception {
        testCreateUser();
        testCreateMindmap();

        listener1.getMindmap().createMagnet(50, 50);

        assertEquals(false, listener1.getMindmap().getMagnetGroups().isEmpty());
        MagnetGroup magnetGroup = listener1.getMindmap().getMagnetGroups().get(0);
        assertEquals(false, magnetGroup.getMagnets().isEmpty());
        assertEquals(true, magnetGroup.getX() == 50 && magnetGroup.getY() == 50);
        assertEquals(true, magnetGroup.getLines().isEmpty());
        assertEquals(true, magnetGroup.getTitle().isEmpty());
        Magnet magnet = magnetGroup.getMagnets().get(0);
    }

    public void testDraw() throws Exception{
        testCreateUser();
        testCreateMindmap();
        testCreateMagnet();


    }
}