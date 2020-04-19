package nl.gerete.tourspel.pages;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import to.etc.domui.component.buttons.*;
import to.etc.domui.component.controlfactory.*;
import to.etc.domui.component.form.*;
import to.etc.domui.component.layout.*;
import to.etc.domui.component.meta.*;
import to.etc.domui.component.misc.*;
import to.etc.domui.dom.errors.*;
import to.etc.domui.dom.html.*;
import to.etc.domui.state.*;
import to.etc.webapp.nls.*;
import to.etc.webapp.query.*;

import nl.gerete.tourspel.pages.adm.*;

public class BasicEditPage<T> extends BasicTourPage {
	private Class<T> m_class;

	private T m_object;

	private String[] m_props;

	private Div m_content;

	private ModelBindings m_mb;

	private Collection<ISavableFragment> m_savableFragments = new ArrayList<ISavableFragment>();

	public BasicEditPage(Class<T> clz, String... props) {
		m_class = clz;
		m_props = props;
	}

	public void setProperties(String... props) {
		m_props = props;
	}

	public T getObject() throws Exception {
		if(m_object == null) {
			QDataContext dc = getSharedContext();
			if(getPage().getPageParameters().getString("id").equals("NEW")) {
				m_object = m_class.newInstance();
				initializeNewInstance(m_object);
			} else {
				Long id = Long.valueOf(getPage().getPageParameters().getString("id"));
				m_object = dc.find(m_class, id);
			}
		}

		return m_object;
	}

	protected void initializeNewInstance(T object) throws Exception {}

	public void setObject(T object) {
		m_object = object;
	}

	@Override
	public void createContent() throws Exception {
		m_content = getTourFrame();
		delegateTo(m_content);
		TabularFormBuilder tfb = new TabularFormBuilder(getObject());

		ClassMetaModel cmm = MetaManager.findClassMeta(m_class);
		if(m_props.length != 0) {
			for(String s : m_props) {
				tfb.addProp(s);
			}
		} else {
			for(PropertyMetaModel< ? > pmm : cmm.getProperties()) {
				/**
				 * Add all the m_class property names to the TabularFormBuilder
				 */
				tfb.addProp(pmm.getName());
			}
		}

		m_mb = tfb.getBindings();
		m_mb.moveModelToControl();
		add(tfb.finish());

		m_content.add(getButtonBar(m_mb));

	}

	protected ButtonBar getButtonBar(final ModelBindings mb) {
		ButtonBar buttonBar = new ButtonBar();
		add(buttonBar);

		buttonBar.addConfirmedButton("Delete", "Are you sure you want to delete this record?", new IClicked<DefaultButton>() {

			@Override
			public void clicked(DefaultButton clickednode) throws Exception {
				m_mb.moveControlToModel();
				QDataContext dc = getSharedContext();
				dc.startTransaction();
				dc.delete(m_object);
				dc.commit();
				UIGoto.back();
			}

		});

		buttonBar.addBackButton();
		buttonBar.addButton("Opslaan", new IClicked<DefaultButton>() {
			@Override
			public void clicked(DefaultButton btn) throws Exception {
				save();
				UIGoto.clearPageAndReload(getPage(), UIMessage.info(BundleRef.create(BasicEditPage.class, "messages"), "global.saved"));
			}

		});
		if(getAdditionalButton() != null) {
			buttonBar.addButton(getAdditionalButton());
		}
		return buttonBar;
	}

	protected void onBeforeSave(T instance) throws Exception {}

	protected void save() throws Exception {
		m_mb.moveControlToModel();
		onBeforeSave(m_object);
		QDataContext dc = getSharedContext();
		dc.startTransaction();
		dc.save(m_object);
		for(ISavableFragment fragment : m_savableFragments) {
			fragment.save(dc);
		}
		dc.commit();
		MessageFlare.display(this, MsgType.INFO, "De gegevens zijn opgeslagen");
	}


	public void addSavableFragment(@NonNull ISavableFragment fragment) {
		m_savableFragments.add(fragment);
	}

	public void removeSavableFragment(@NonNull ISavableFragment fragment) {
		m_savableFragments.remove(fragment);
	}

	public DefaultButton getAdditionalButton() {
		return null;
	}

}

