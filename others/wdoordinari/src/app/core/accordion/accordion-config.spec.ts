import { AccordionConfig } from './accordion-config';

describe('ngb-accordion-config', () => {
  it('should have sensible default values', () => {
    const config = new AccordionConfig();

    expect(config.closeOthers).toBe(false);
    expect(config.type).toBeUndefined();
  });
});
