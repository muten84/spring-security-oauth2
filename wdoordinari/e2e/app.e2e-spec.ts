import { WdoordinariPage } from './app.po';

describe('wdoordinari App', () => {
  let page: WdoordinariPage;

  beforeEach(() => {
    page = new WdoordinariPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
