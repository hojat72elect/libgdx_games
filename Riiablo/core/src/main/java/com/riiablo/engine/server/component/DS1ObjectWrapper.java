package com.riiablo.engine.server.component;

import com.artemis.Component;
import com.artemis.annotations.PooledWeaver;
import com.artemis.annotations.Transient;
import com.riiablo.map.DS1;

@Transient
@PooledWeaver
public class DS1ObjectWrapper extends Component {
  public DS1        ds1;
  public DS1.Object object;

  public DS1ObjectWrapper set(DS1 ds1, DS1.Object object) {
    this.ds1 = ds1;
    this.object = object;
    return this;
  }
}
