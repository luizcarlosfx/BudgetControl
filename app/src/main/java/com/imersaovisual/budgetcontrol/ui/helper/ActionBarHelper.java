package com.imersaovisual.budgetcontrol.ui.helper;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Action bar helper for use on ICS and newer devices.
 */
public class ActionBarHelper
{
	private final ActionBar mActionBar;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	public ActionBarHelper(ActionBarActivity activity)
	{
		this.mActionBar = activity.getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mTitle = mDrawerTitle = activity.getTitle();
	}

	/**
	 * When the drawer is closed we restore the action bar state reflecting the
	 * specific contents in view.
	 */
	public void onDrawerClosed()
	{
		mActionBar.setTitle(mTitle);
	}

	/**
	 * When the drawer is open we set the action bar to a generic title. The
	 * action bar should only contain data relevant at the top level of the nav
	 * hierarchy represented by the drawer, as the rest of your content will be
	 * dimmed down and non-interactive.
	 */
	public void onDrawerOpened()
	{
		mActionBar.setTitle(mDrawerTitle);
	}

	public void setTitle(CharSequence title)
	{
		mTitle = title;
	}
}